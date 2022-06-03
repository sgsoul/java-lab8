package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;

import collection.HumanObservableManager;
import commands.ClientCommandManager;
import common.auth.User;
import common.connection.*;
import common.exceptions.*;
import common.io.OutputManager;
import controllers.tools.ObservableResourceFactory;

import static common.io.ConsoleOutputter.print;
import static common.io.ConsoleOutputter.printErr;

/**
 * ����� �������.
 */

public class Client extends Thread implements SenderReceiver {
    private SocketAddress address;
    private InetSocketAddress host;
    private DatagramSocket socket;
    private DatagramSocket broadcastSocket;
    public final int MAX_TIME_OUT = 500;
    public final int MAX_ATTEMPTS = 3;
    private User user;
    private User attempt;
    private boolean running;

    private ClientCommandManager commandManager;
    private OutputManager outputManager;
    private volatile boolean authSuccess;
    private volatile boolean receivedRequest;
    private ObservableResourceFactory resourceFactory;
    public boolean isReceivedRequest(){
        return receivedRequest;
    }
    private boolean connected;
    private HumanObservableManager collectionManager;

    /**
     * ������������� �������.
     * @param addr
     * @param p
     * @throws ConnectionException
     */

    private void init(String addr, int p) throws ConnectionException {
        connect(addr, p);
        running = true;
        connected = false;
        authSuccess = false;
        collectionManager = new HumanObservableManager();
        commandManager = new ClientCommandManager(this);
        setName("������ �� �����.");
    }

    public Client(String addr, int p) throws ConnectionException {
        init(addr, p);
    }

    public void setUser(User usr) {
        user = usr;
    }

    public User getUser() {
        return user;
    }

    public void setAttemptUser(User u) {
        attempt = u;
    }

    public User getAttemptUser() {
        return attempt;
    }

    /**
     * ����������� � �������.
     * @param addr
     * @param p
     * @throws ConnectionException
     */

    public void connect(String addr, int p) throws ConnectionException {
        try {
            address = new InetSocketAddress(InetAddress.getByName(addr), p);
        } catch (UnknownHostException e) {
            throw new InvalidAddressException();
        } catch (IllegalArgumentException e) {
            throw new InvalidPortException();
        }
        try {
            socket = new DatagramSocket();
            broadcastSocket = new DatagramSocket();
            host = new InetSocketAddress(InetAddress.getByName("localhost"), broadcastSocket.getLocalPort());
            socket.setSoTimeout(MAX_TIME_OUT);
        } catch (IOException e) {
            throw new ConnectionException("�� ������� ������� �����");
        }
    }

    /**
     * ������ �������.
     * @param request
     * @throws ConnectionException
     */

    public void send(Request request) throws ConnectionException {
        try {
            request.setBroadcastAddress(host);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objOutput = new ObjectOutputStream(byteArrayOutputStream);
            objOutput.writeObject(request);
            DatagramPacket requestPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), address);
            socket.send(requestPacket);
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new ConnectionException("���-�� ����� �� ��� ��� �������� �������");
        }
    }

    /**
     * ��������� ������.
     * @return
     * @throws ConnectionException
     * @throws InvalidDataException
     */

    public Response receive() throws ConnectionException, InvalidDataException {
        connected = false;
        try {
            socket.setSoTimeout(MAX_TIME_OUT);
        } catch (SocketException ignored) {

        }
        ByteBuffer bytes = ByteBuffer.allocate(BUFFER_SIZE);
        DatagramPacket receivePacket = new DatagramPacket(bytes.array(), bytes.array().length);
        try {
            socket.receive(receivePacket);
        } catch (SocketTimeoutException e) {
            for (int attempts = MAX_ATTEMPTS; attempts > 0; attempts--) {
                try {
                    socket.receive(receivePacket);
                    break;
                } catch (IOException ignored) {
                }
            }
            throw new ConnectionTimeoutException();
        } catch (IOException e) {
            throw new ConnectionException("���-�� ����� �� ��� ��� ��������� ������");
        }
        connected = true;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes.array()));
            return (Response) objectInputStream.readObject();
        } catch (ClassNotFoundException | ClassCastException | IOException e) {
            throw new InvalidReceivedDataException();
        }
    }

    /**
     * ����� ����������������� ��������
     * @return
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    private Response receiveBroadcast() throws ConnectionException, InvalidDataException{
        try {
            broadcastSocket.setSoTimeout(0);
        } catch (SocketException ignored) {

        }
        ByteBuffer bytes = ByteBuffer.allocate(BUFFER_SIZE);
        DatagramPacket receivePacket = new DatagramPacket(bytes.array(), bytes.array().length);
        try {
            broadcastSocket.receive(receivePacket);
        } catch (IOException e) {
            throw new ConnectionException("���-�� ����� �� ��� ��� ��������� ������");
        }
        connected=true;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes.array()));
            return (Response) objectInputStream.readObject();
        } catch (ClassNotFoundException | ClassCastException | IOException e) {
            throw new InvalidReceivedDataException();
        }
    }

    /**
     * ������
     */

    @Override
    public void run() {
        Request hello = new CommandMsg();
        hello.setStatus(Request.Status.HELLO);
        try {
            send(hello);
            Response response = receive();
            if (response.getStatus() == Response.Status.COLLECTION && response.getCollection() != null && response.getCollectionOperation() == CollectionOperation.ADD) {
                collectionManager.applyChanges(response);
            }
        } catch (ConnectionException | InvalidDataException e) {
            printErr("�� ������� ��������� ��������� � �������");
        }
        while (running) {
            try {
                receivedRequest = false;
                Response response = receiveBroadcast();
                String msg = response.getMessage();
                switch (response.getStatus()) {
                    case COLLECTION:
                        collectionManager.applyChanges(response);
                        print("���������!");
                        break;
                    case BROADCAST:
                        print("��������� ����!");
                        collectionManager.applyChanges(response);
                        break;
                    case AUTH_SUCCESS:
                        user = attempt;
                        authSuccess = true;
                        break;
                    case EXIT:
                        connected = false;
                        print("������ ��������");
                        outputManager.error("[ServerShutDown]");
                        break;
                    case FINE:
                        outputManager.info(msg);
                        break;
                    case ERROR:
                        outputManager.error(msg);

                    default:
                        print(msg);
                        receivedRequest = true;
                        break;
                }

            } catch (ConnectionException | InvalidDataException ignored) {
            }
        }
    }

    /**
     * �������� ����������
     */

    public void connectionTest() {
        connected = false;
        try {
            send(new CommandMsg().setStatus(Request.Status.CONNECTION_TEST));
            Response response = receive();
            connected = (response.getStatus() == Response.Status.FINE);
        } catch (ConnectionException | InvalidDataException ignored) {

        }
    }

    /**
     * �������� ��������������
     * @param login
     * @param password
     * @param register
     */

    public void processAuthentication(String login, String password, boolean register) {
        attempt = new User(login, password);
        CommandMsg msg;
        if (register) {
            msg = new CommandMsg("register").setStatus(Request.Status.DEFAULT).setUser(attempt);
        } else {
            msg = new CommandMsg("login").setStatus(Request.Status.DEFAULT).setUser(attempt);
        }
        try {
            send(msg);
            Response answer = receive();
            connected = true;
            authSuccess = (answer.getStatus() == Response.Status.AUTH_SUCCESS);
            if (authSuccess) {
                user = attempt;
            } else {
                outputManager.error(!register ? "[AuthException]" : "[RegisterException] " + "[" + getAttemptUser() + "]");
            }
        } catch (ConnectionTimeoutException e) {
            outputManager.error("[TimeoutException]");
            connected = false;
        } catch (ConnectionException | InvalidDataException e) {
            connected = false;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isAuthSuccess() {
        return authSuccess;
    }

    public void setAuthSuccess(boolean f) {
        authSuccess = f;
    }

    public HumanObservableManager getHumanManager() {
        return collectionManager;
    }

    public ClientCommandManager getCommandManager() {
        return commandManager;
    }

    public OutputManager getOutputManager() {
        return outputManager;
    }

    public void setOutputManager(OutputManager out) {
        outputManager = out;
    }

    public ObservableResourceFactory getResourceFactory() {
        return resourceFactory;
    }

    public void setResourceFactory(ObservableResourceFactory rf) {
        resourceFactory = rf;
    }

    /**
     * �������� �������
     */

    public void close() {
        try {
            send(new CommandMsg().setStatus(Request.Status.EXIT));
        } catch (ConnectionException ignored) {

        }
        running = false;
        commandManager.close();
        socket.close();
        broadcastSocket.close();
    }

}