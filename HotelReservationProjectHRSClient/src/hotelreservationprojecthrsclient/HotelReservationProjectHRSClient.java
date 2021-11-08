/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojecthrsclient;

import entity.Partner;
import java.util.Scanner;
import util.enumeration.PartnerType;
import util.exceptions.LoginCredentialsInvalidException;

/**
 *
 * @author ryo20
 */
public class HotelReservationProjectHRSClient {

    private static Partner currentPartner;

    public static void main(String[] args) {
        runMainMenu();
    }

    public static void runMainMenu() {

        Scanner sc = new Scanner(System.in);
        int response;

        while (true) {
            System.out.println("\nWelcome to the Holiday Reservation System for Partners!");
            System.out.println("-------------------------------------------------------\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            while (true) {
                System.out.print("Enter an option> ");

                try {
                    response = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException ex) {
                    response = 404;
                }

                if (response == 1) {
                    try {
                        doLogin();
                        if (currentPartner.getPartnerType() == PartnerType.PARTNEREMPLOYEE) {
                            runPartnerEmployeeMenu();
                        } else if (currentPartner.getPartnerType() == PartnerType.PARTNERRESERVATIONMANAGER) {
                            runPartnerManagerMenu();
                        }
                    } catch (LoginCredentialsInvalidException ex) {
                        System.out.println("Invalid login credentials!");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }

            if (response == 2) {
                System.out.println("Exiting application!");
                break;
            }
        }
    }

    public static void doLogin() throws LoginCredentialsInvalidException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        System.out.println("\nYou are now logging in!");
        System.out.println("-----------------------\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            //call web service
        } else {
            throw new LoginCredentialsInvalidException("Invalid login credential!");
        }
    }

    private static void runPartnerEmployeeMenu() {
        System.out.println("\nYou are logged in as a Partner Employee!");
        System.out.println("----------------------------------------\n");
    }

    private static void runPartnerManagerMenu() {
        System.out.println("\nYou are logged in as a Partner Manager!");
        System.out.println("---------------------------------------\n");
    }
}
