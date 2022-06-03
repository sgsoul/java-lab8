package common.io;

import common.auth.User;
import common.data.Car;
import common.data.Coordinates;
import common.data.WeaponType;

import java.util.Scanner;

import static common.io.ConsoleOutputter.print;

public class ConsoleInputManager extends InputManagerImpl {

    public ConsoleInputManager() {
        super(new Scanner(System.in));
        getScanner().useDelimiter("\n");
    }

    @Override
    public String readName() {
        return new Question<>("Введите имя:", super::readName).getAnswer();
    }

    @Override
    public String readFullName() {
        return new Question<>("Введите фамилию:", super::readFullName).getAnswer();
    }

    @Override
    public double readXCoord() {
        return new Question<>("Введите координату х:", super::readXCoord).getAnswer();
    }

    @Override
    public double readYCoord() {
        return new Question<>("Введите координату у:", super::readYCoord).getAnswer();
    }

    @Override
    public Coordinates readCoords() {
        print("Введите координаты.");
        double x = readXCoord();
        double y = readYCoord();
        return new Coordinates(x, y);
    }

    @Override
    public boolean readRealHero() {
        return new Question<>("Герой реальный? (true/false): ", super::readRealHero).getAnswer();
    }

    @Override
    public boolean readHasToothPick() {
        return new Question<>("У него есть зубочистка? (true/false):", super::readHasToothPick).getAnswer();
    }

    @Override
    public Integer readImpactSpeed() {
        return new Question<>("Введите скорость удара:", super::readImpactSpeed).getAnswer();
    }


    @Override
    public String readSoundtrackName() {
        return new Question<>("Введите название песни:", super::readSoundtrackName).getAnswer();
    }

    @Override
    public float readMinutesOfWaiting() {
        return new Question<>("Введите количество минут ожидания:", super::readMinutesOfWaiting).getAnswer();
    }

    @Override
    public WeaponType readWeaponType() {
        return new Question<>("Введите тип оружия (AXE, PISTOL, SHOTGUN):", super::readWeaponType).getAnswer();
    }

    @Override
    public Car readCar() {
        return new Question<>("Введите машину:", super::readCar).getAnswer();
    }

    @Override
    public String readLogin() {
        return new Question<>("enter login:", super::readLogin).getAnswer();
    }

    @Override
    public String readPassword() {
        return new Question<>("enter password:", super::readPassword).getAnswer();
    }

    @Override
    public User readUser() {
        String login = readLogin();
        String password = readPassword();
        return new User(login, password);
    }
}
