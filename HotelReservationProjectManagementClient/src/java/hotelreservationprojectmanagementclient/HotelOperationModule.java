/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojectmanagementclient;

import ejb.session.stateful.HotelReservationBeanRemote;
import ejb.session.stateless.BookingSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.enumeration.RateType;
import static util.enumeration.RateType.NORMALRATE;
import static util.enumeration.RateType.PEAKRATE;
import static util.enumeration.RateType.PROMOTIONRATE;
import static util.enumeration.RateType.PUBLISHRATE;
import util.exceptions.FailedToCreateRoomRateException;
import util.exceptions.RoomIsTiedToABookingDeletionException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
public class HotelOperationModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private BookingSessionBeanRemote bookingSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;

    private Employee currentEmployee;

    public HotelOperationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote,
            BookingSessionBeanRemote bookingSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote,
            RoomRateSessionBeanRemote roomRateSessionBeanRemote, Employee currentEmployee) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.bookingSessionBeanRemote = bookingSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void runMainMenu() {
        if (currentEmployee.getErole() == EmployeeRole.OPERATIONMANAGER) {
            this.runOperationsMenu();
        } else if (currentEmployee.getErole() == EmployeeRole.SALESMANAGER) {
            this.runSalesMenu();
        }
    }

    private void runOperationsMenu() {
        Scanner sc = new Scanner(System.in);
        boolean logout = false;
        int submoduleOption;
        while (!logout) {
            System.out.println("\n-You are currently logged in as a Operation Manager-");
            System.out.println("----------------------------------------------------\n");
            System.out.println("1. Room Types");
            System.out.println("2. Rooms");
            System.out.println("3. Logout");
            System.out.print("Please select a sub-module to work with>");

            try {
                submoduleOption = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                submoduleOption = 404;
            }

            if (submoduleOption == 1) {
                boolean returnBack = false;
                while (!returnBack) {
                    int option;
                    System.out.println("\n-You are now in the Room Type Sub-Module-");
                    System.out.println("-----------------------------------------\n");
                    System.out.println("1. Create new Room Type");
                    System.out.println("2. Update a Room Type");
                    System.out.println("3. Delete a Room Type");
                    System.out.println("4. View All Room Types");
                    System.out.println("5. View Room Type details");
                    System.out.println("6. Back to sub-module page");
                    System.out.print("Please select an option>");

                    try {
                        option = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException ex) {
                        option = 404;
                    }

                    switch (option) {
                        case 1:
                            this.createNewRoomType(sc);
                            break;
                        case 2:
                            this.updateARoomType(sc);
                            break;
                        case 3:
                            this.deleteARoomType(sc);
                            break;
                        case 4:
                            try {
                            this.viewAllRoomTypes();
                        } catch (RoomTypeNotFoundException ex) {
                            System.out.println("No Room Types in the Database!");
                        }
                        break;
                        case 5:
                            this.viewRoomTypeDetails(sc);
                            break;
                        case 6:
                            returnBack = true;
                            break;
                        default:
                            System.out.println("Please input a valid choice");
                    }
                }
            } else if (submoduleOption == 2) {
                boolean returnBack = false;
                while (!returnBack) {
                    int option;
                    System.out.println("\n-You are now in the Room Sub-Module-");
                    System.out.println("------------------------------------\n");
                    System.out.println("1. Create new Room");
                    System.out.println("2. Update a Room");
                    System.out.println("3. Delete a Room");
                    System.out.println("4. View All Rooms");
                    System.out.println("5. View Room Allocation Exception Report");
                    System.out.println("6. Back to sub-module page");
                    System.out.print("Please select an option>");

                    try {
                        option = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException ex) {
                        option = 404;
                    }

                    switch (option) {
                        case 1:
                            try {
                            this.createNewRoom(sc);
                        } catch (RoomTypeNotFoundException ex) {
                            System.out.println("Room Type not found!");
                        }
                        break;
                        case 2:
                            this.updateARoom(sc);
                            break;
                        case 3:
                            this.deleteARoom(sc);
                            break;
                        case 4:
                            this.viewAllRooms(sc);
                            break;
                        case 5:
                            this.viewRoomAllocationExceptionReport(sc);
                            break;
                        case 6:
                            returnBack = true;
                            break;
                        default:
                            System.out.println("Please input a valid choice");
                    }
                }
            } else if (submoduleOption == 3) {
                logout = true;
                break;
            } else {
                System.out.println("Select a valid sub-module option!");
            }
        }
    }

    private void runSalesMenu() {
        Scanner sc = new Scanner(System.in);
        boolean logout = false;
        int option;

        while (!logout) {
            System.out.println("\n-You are currently logged in as a Sales Manager-");
            System.out.println("------------------------------------------------\n");
            System.out.println("1. Create new Room Rate");
            System.out.println("2. Update a Room Rate");
            System.out.println("3. Delete a Room Rates");
            System.out.println("4. View all Room Rates");
            System.out.println("5. View Room Rates details");
            System.out.print("Please select an option>");

            try {
                option = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                option = 404;
            }

            switch (option) {
                case 1:
                    this.createNewRoomRate(sc);
                    break;
                case 2:
                    this.updateARoomRate(sc);
                    break;
                case 3:
                    this.deleteARoomRate(sc);
                    break;
                case 4:
                    this.viewAllRoomRates();
                    break;
                case 5:
                    this.viewRoomRateDetails(sc);
                    break;
                case 6:
                    logout = true;
                    break;
                default:
                    System.out.println("Please input a valid option!");
            }
        }
    }

    private void createNewRoomType(Scanner sc) {
        // ideally when given a nextHigherRoomType name we want to call getRoomTypeByName(name), should throw RoomTypeNotFoundException if not found
        // when catching this exception, we can either ask for reinput in a loop or cancel transaction, then call createNewRoomType(RoomType roomType, long nextHighertTypeId)
        // Not doing both of these in 1 method prevents the user from enetering a bunch of info just for the roomtype not to exist at the end.
        // also for create methods, the only object we should pass in is the one we are creating, any associating object should be an id (theo).
        String name;
        String description;
        String roomSize;
        String nextHigherRoomTypeName;
        RoomType nextHigherRoomType = null;
        int numOfBeds = 0;
        int roomCapacity = 0;

        try {
            System.out.println("\n-You are now creating a new room type-");
            System.out.println("--------------------------------------\n");
            System.out.print("Please enter a Room Type name>");
            name = sc.nextLine().trim();

            System.out.println("Please enter a next higher room type by name(case-sensitive), input 'None' if this will be the higher rank");
            nextHigherRoomTypeName = sc.nextLine().trim();

            if (!nextHigherRoomTypeName.equals("None")) {
                nextHigherRoomType = roomTypeSessionBeanRemote.getRoomTypeByName(nextHigherRoomTypeName);
            }

            System.out.print("Please enter a Room Type Description>");
            description = sc.nextLine().trim();
            System.out.print("Please enter a Room Type Size>");
            roomSize = sc.nextLine().trim();

            while (numOfBeds != 404) {
                System.out.print("Please input the number of beds (Integer)>");
                try {
                    numOfBeds = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException ex) {
                    System.out.println("Please enter an integer!");
                    numOfBeds = 404;
                }
            }

            while (roomCapacity != 404) {
                System.out.print("Please input the room capacity (Integer)>");
                try {
                    roomCapacity = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException ex) {
                    System.out.println("Please enter an integer!");
                    roomCapacity = 404;
                }
            }

            RoomType newRoomType = new RoomType(name, description, roomSize, numOfBeds, roomCapacity);
            
            if (nextHigherRoomType != null) { //Saved as a String
                newRoomType.setNextHigherRoomType(nextHigherRoomTypeName);
            }
            
            //adding amenities
            boolean hasMoreAmenities = true;
            while (hasMoreAmenities) {
                String response = "";
                System.out.print("Please enter an amenity: ");
                String amenity = sc.nextLine().trim();
                newRoomType.getAmenities().add(amenity);

                while (true) {
                    System.out.print("Do you have more amenities to add? (yes/no)>");
                    response = sc.nextLine().trim();
                    if (response.equals("no")) {
                        hasMoreAmenities = false;
                        break;
                    } else if (response.equals("yes")) {
                        break;
                    } else {
                        System.out.println("Please enter a valid response!");
                    }
                }
            }

        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Next Higher Room Type is not found!");
        }
    }

    private void updateARoomType(Scanner sc) {
        System.out.println("\n-You are now updating a Room Type-");
        System.out.println("----------------------------------\n");

        String roomTypeName = sc.nextLine().trim();
        try {
            RoomType roomType = roomTypeSessionBean.getRoomTypeByName(roomTypeName);
            System.out.print("Select a New Room Type name>");
            String newRoomTypeName = sc.nextLine().trim();
            System.out.print("Select a New Room Type ranking>");
            Integer newRoomTypeRanking = sc.nextInt();
            Integer ranking = newRoomTypeRanking;
            Integer oldRanking = roomType.getNextHigherRoomType();
            List<RoomType> roomTypeBellowRanking = roomTypeSessionBean.getRoomTypeBetweenRanking(newRoomTypeRanking, oldRanking);
            for (RoomType updateroomType : roomTypeBellowRanking) {
                try {
                    roomTypeSessionBean.updateRoomType(updateroomType.getRoomTypeId(), updateroomType.getRoomName(), ranking + 1);
                } catch (RoomTypeNotFoundException ex) {
                    System.out.println("room type not found!");
                }
                ranking++;
            }
            roomTypeSessionBean.updateRoomType(roomType.getRoomTypeId(), newRoomTypeName, newRoomTypeRanking);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("room type not found!");
        }
    }

    private void deleteARoomType(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void viewAllRoomTypes() throws RoomTypeNotFoundException {
        System.out.println("You are now viewing all Room Types");
        List<RoomType> listOfRoomTypes = roomTypeSessionBean.retrieveRoomTypes();
        for (int i = 1; i <= listOfRoomTypes.size(); i++) {
            System.out.println(i + ". " + listOfRoomTypes.get(i - 1));
        }
    }

    private void viewRoomTypeDetails(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void createNewRoom(Scanner sc) throws RoomTypeNotFoundException {
        System.out.println("\n-You are now creating a new Room-");
        System.out.println("---------------------------------\n");
        System.out.print("Please enter a floor>");
        String floor = sc.nextLine().trim();
        System.out.print("Please enter a room number>");
        String number = sc.nextLine().trim();
        String roomNumber = String.join(floor, number);
        this.viewAllRoomTypes();
        System.out.println("Please enter a room type");
        String roomTypeName = sc.nextLine();
        RoomType roomType = roomTypeSessionBean.getRoomTypeByName(roomTypeName);
        Room room = new Room(roomNumber, roomType);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void updateARoom(Scanner sc) {
        System.out.println("You are now updating a Room");
        System.out.println("Please enter a room number");
        String roomNumber = sc.nextLine().trim();
        try {
            Room room = roomSessionBean.getRoomByRoomNumber(roomNumber);
            int option;
            while (true) {
                System.out.println("1. Update Room Number");
                System.out.println("2. Update Room status");
                System.out.print("Enter your choice>");
                option = sc.nextInt();

                if (option == 1) {
                    System.out.print("Please enter a floor>");
                    String floor = sc.nextLine().trim();
                    System.out.print("Please enter a room number>");
                    String number = sc.nextLine().trim();
                    String newRoomNumber = String.join(floor, number);
                    room.setRoomNumber(newRoomNumber);
                    roomSessionBean.updateRoom(room);

                } else if (option == 2) {
                    if (room.getRoomStatus()) {
                        System.out.println("Room status changed from in-use to not-in-use");
                        room.setRoomStatus(true);
                    } else {
                        System.out.println("Room changed from not-in-use to in-use");
                        room.setRoomStatus(false);
                    }
                }
            }
        } catch (RoomNotFoundException ex) {
            System.out.println("Invalid room number!");
        }
    }

    private void deleteARoom(Scanner sc) {
        System.out.println("You are now deleting a Room");
        System.out.println("Please enter a room number");
        String roomNumber = sc.nextLine().trim();
        try {
            roomSessionBean.deleteRoomByRoomNumber(roomNumber);
        } catch (RoomNotFoundException ex) {
            System.out.println("Room not found!");
        } catch (RoomIsTiedToABookingDeletionException ex) {
            System.out.println("Room is currently tied to a booking!");
        }
    }

    private void viewAllRooms(Scanner sc) {
        try {
            List<Room> rooms = roomSessionBean.retrieveRooms();
            for (Room r : rooms) {
                String status;
                if (r.getRoomStatus()) {
                    status = "Occupied";
                } else {
                    status = "Empty";
                }
                System.out.println("Room number: " + r.getRoomNumber() + " Room Type: " + r.getRoomType().getRoomName() + " Room status: " + status);
            }
        } catch (RoomNotFoundException ex) {
            System.out.println("Room not found!");
        }
    }

    private void viewRoomAllocationExceptionReport(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void createNewRoomRate(Scanner sc) {
        System.out.println("You are now creating a new Room Rate");
        System.out.println("Please enter a rate type:");
        for (int i = 0; i < RateType.values().length; i++) {
            System.out.println((i + 1) + ". " + RateType.values()[i]);
        }

        RateType rate;
        double price;
        String startDateString;
        String endDateString;
        Date startDate;
        Date endDate;

        System.out.print("Select a rate from 1-4>");
        int option = sc.nextInt();
        while (true) {
            if (option == 1) {
                rate = RateType.PUBLISHRATE;
                break;
            } else if (option == 2) {
                rate = RateType.PEAKRATE;
                break;
            } else if (option == 3) {
                rate = RateType.NORMALRATE;
                break;
            } else if (option == 4) {
                rate = RateType.PROMOTIONRATE;
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
        System.out.print("Input a price>");
        price = sc.nextDouble();
        System.out.print("Input Start Date in dd/mm/yyyy (with the slashes)>");
        startDateString = sc.next();
        System.out.print("Input End Date in dd/mm/yyyy (with the slashes)>");
        endDateString = sc.next();

        int[] sDate = Arrays.stream(startDateString.split("/")).mapToInt(Integer::parseInt).toArray();
        int[] eDate = Arrays.stream(endDateString.split("/")).mapToInt(Integer::parseInt).toArray();
        startDate = new Date(sDate[3], sDate[2], sDate[1]);
        endDate = new Date(eDate[3], eDate[2], eDate[1]);
        int roomTypeRank;

        System.out.print("Which room type to set this room rate to?>");
        try {
            this.viewAllRoomTypes();
            System.out.println("Room Types are ordered by rank, select a rank (numeric): ");
            roomTypeRank = sc.nextInt();
            RoomRate newRate = new RoomRate(rate, price, startDate, endDate);
            roomRateSessionBeanRemote.createNewRoomRate(newRate, roomTypeRank);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Operation cancelled! No room types exist in the database");
        } catch (FailedToCreateRoomRateException ex) {
            System.out.println("Failed to create Room Rate! Please check that a correct room rank was entered");
        }
    }

    private void updateARoomRate(Scanner sc) {
        System.out.println("You are now updating a Room Rate");
        String roomNumber = sc.nextLine().trim();
        try {
            this.viewAllRoomTypes();
            System.out.print("Please enter a room type option by rank(numeric)>");
            int roomTypeRanking = sc.nextInt();
            RateType rate;
            this.viewAllRoomRates();
            System.out.print("Select a rate from 1-4>");
            int option = sc.nextInt();
            while (true) {
                if (option == 1) {
                    rate = RateType.PUBLISHRATE;
                    break;
                } else if (option == 2) {
                    rate = RateType.PEAKRATE;
                    break;
                } else if (option == 3) {
                    rate = RateType.NORMALRATE;
                    break;
                } else if (option == 4) {
                    rate = RateType.PROMOTIONRATE;
                    break;
                } else {
                    System.out.println("Invalid choice!");
                }
            }
            List<RoomRate> listOfFilteredRates = roomTypeSessionBean.getRoomRateByRoomTypeRankAndRateType(roomTypeRanking, rate);

            int roomRateToEditChoice;
            RoomRate roomRateToEdit;
            int scenario;

            if (listOfFilteredRates.size() > 1) { //if room rate is of type promotionRate or peakRate
                while (true) {
                    for (int i = 0; i < listOfFilteredRates.size(); i++) {
                        System.out.println((i + 1) + ". Rate Type: " + listOfFilteredRates.get(i).getRateType() + " Rate Per Night: " + listOfFilteredRates.get(i).getPrice() + " Start Date: "
                                + listOfFilteredRates.get(i).getStartDate() + " End Date: " + listOfFilteredRates.get(i).getEndDate());
                    }
                    System.out.print("Please chose a rate to edit by index>");
                    roomRateToEditChoice = sc.nextInt();

                    try {
                        roomRateToEdit = listOfFilteredRates.get(roomRateToEditChoice);
                        scenario = 1;
                        break;
                    } catch (IndexOutOfBoundsException ex) {
                        System.out.println("Please select a valid Room Rate!");
                    }
                }
            } else { // if room rate is of type publishRate or normalRate
                roomRateToEdit = listOfFilteredRates.get(0);
                scenario = 2;
            }

            boolean moreToEdit = true;
            while (moreToEdit) {
                int editOption;

                while (true) {
                    if (scenario == 1) {
                        System.out.println("1. Edit the RateType");
                        System.out.println("2. Edit the Price");
                        System.out.print("Enter a choice>");
                        editOption = sc.nextInt();
                        if (editOption == 1 && editOption == 2) {
                            break;
                        }

                    } else if (scenario == 2) {
                        System.out.println("1. Edit the RateType");
                        System.out.println("2. Edit the Price");
                        System.out.println("3. Edit the Start Date and End Date");
                        System.out.print("Enter a choice>");
                        editOption = sc.nextInt();
                        if (editOption > 0 && editOption < 4) {
                            break;
                        }
                    }
                    System.out.println("Enter a valid option!");
                }

                if (editOption == 1) { //editing a rate
                    System.out.println("Current Rate Type is: " + roomRateToEdit.getRateType().toString());
                    int newRateChoice;
                    RateType newRate = null;
                    while (true) {
                        System.out.print("Please choose a new rate type>");
                        this.viewAllRoomRates();
                        newRateChoice = sc.nextInt();
                        if (newRateChoice == 1) {
                            newRate = RateType.PUBLISHRATE;
                            break;
                        } else if (newRateChoice == 2) {
                            newRate = RateType.PEAKRATE;
                            break;
                        } else if (newRateChoice == 3) {
                            newRate = RateType.NORMALRATE;
                            break;
                        } else if (newRateChoice == 4) {
                            newRate = RateType.PROMOTIONRATE;
                            break;
                        } else {
                            System.out.println("Invalid choice!");
                        }
                    }
                    roomRateToEdit.setRateType(newRate);

                } else if (editOption == 2) { //editing a price
                    System.out.println("The old rate is: $" + roomRateToEdit.getPrice());
                    double newPrice = sc.nextDouble();
                    roomRateToEdit.setPrice(newPrice);

                } else if (editOption == 3) { //editing a start date
                    while (true) {
                        System.out.println("The old Start Date is: " + roomRateToEdit.getStartDate());
                        System.out.println("The old End Date is: " + roomRateToEdit.getEndDate());
                        System.out.print("Input new Start Date in dd/mm/yyyy (with the slashes)>");
                        String startDateString = sc.next();
                        System.out.print("Input new End Date in dd/mm/yyyy (with the slashes)>");
                        String endDateString = sc.next();
                        int[] sDate = Arrays.stream(startDateString.split("/")).mapToInt(Integer::parseInt).toArray();
                        int[] eDate = Arrays.stream(endDateString.split("/")).mapToInt(Integer::parseInt).toArray();
                        Date startDate = new Date(sDate[3], sDate[2], sDate[1]);
                        Date endDate = new Date(eDate[3], eDate[2], eDate[1]);
                        if (startDate.compareTo(roomRateToEdit.getEndDate()) > 0) {
                            System.out.println("Invalid Operation - start date exceed end date");
                        } else {
                            roomRateToEdit.setStartDate(startDate);
                            roomRateToEdit.setEndDate(endDate);
                            break;
                        }
                    }
                }
                System.out.println("Do you have anything else to edit? (yes/no)");
                String choice = sc.nextLine().trim();
                while (true) {
                    if (choice.equals("no")) {
                        moreToEdit = false;
                        break;
                    } else if (choice.equals("yes")) {
                        break;
                    } else {
                        System.out.println("Your choices are either 'yes' or 'no'!");
                    }
                }
            }
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("No Room type in database!");
        } catch (RoomRateNotFoundException ex) {
            System.out.println("No Room Rates exist with the provided parameters!");
        }
    }

    private void deleteARoomRate(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void viewAllRoomRates() {
        System.out.println("Displaying list of Room Rates");
        List<RoomRate> listOfRoomRates = roomRateSessionBeanRemote.retrieveRoomRates();
        if (!listOfRoomRates.isEmpty()) {
            for (RoomRate rr : listOfRoomRates) {
                System.out.println("Rate Type: " + rr.getRateType() + " Rate Per Night: " + rr.getPrice() + " Start Date: " + rr.getStartDate() + " End Date: " + rr.getEndDate());
            }
        } else {
            System.out.println("No Room Rate exists in the database!");
        }
    }

    private void viewRoomRateDetails(Scanner sc) {

        System.out.println("You are now viewing a Room Rate");
        System.out.println("Please enter Room Type name");
        String roomName = sc.nextLine().trim();
        try {
            RoomType roomType = roomTypeSessionBean.getRoomTypeByName(roomName);
            while (true) {
                System.out.println("Please enter Room Type's room rate (1: Publish Rate, 2: Normal Rate, 3: Peak Rate, 4: Promotion Rate, 5: Exit)");
                Integer response = sc.nextInt();
                if (response == 1) {
                    List<RoomRate> roomRates = roomTypeSessionBean.getRoomRate(roomName, PUBLISHRATE);
                    if (!roomRates.isEmpty()) {
                        for (RoomRate roomRate : roomRates) {
                            System.out.println("Room Type Name: " + roomName);
                            System.out.println("Rate Type: " + PUBLISHRATE);
                            System.out.println("Rate per night: " + roomRate.getPrice());
                        }
                    } else {
                        System.out.println("Rate Type does not exists!");
                    }
                } else if (response == 2) {
                    List<RoomRate> roomRates = roomTypeSessionBean.getRoomRate(roomName, NORMALRATE);
                    if (!roomRates.isEmpty()) {
                        for (RoomRate roomRate : roomRates) {
                            System.out.println("Room Type Name: " + roomName);
                            System.out.println("Rate Type: " + NORMALRATE);
                            System.out.println("Rate per night: " + roomRate.getPrice());
                        }
                    } else {
                        System.out.println("Rate Type does not exists!");
                    }
                } else if (response == 3) {
                    List<RoomRate> roomRates = roomTypeSessionBean.getRoomRate(roomName, PEAKRATE);
                    if (!roomRates.isEmpty()) {
                        for (RoomRate roomRate : roomRates) {
                            System.out.println("Room Type Name: " + roomName);
                            System.out.println("Rate Type: " + PEAKRATE);
                            System.out.println("Rate per night: " + roomRate.getPrice());
                        }
                    } else {
                        System.out.println("Rate Type does not exists!");
                    }
                } else if (response == 4) {
                    List<RoomRate> roomRates = roomTypeSessionBean.getRoomRate(roomName, PROMOTIONRATE);
                    if (!roomRates.isEmpty()) {
                        for (RoomRate roomRate : roomRates) {
                            System.out.println("Room Type Name: " + roomName);
                            System.out.println("Rate Type: " + PROMOTIONRATE);
                            System.out.println("Rate per night: " + roomRate.getPrice());
                        }
                    } else {
                        System.out.println("Rate Type does not exists!");
                    }
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid Room Rate!");
                }
            }
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Operation cancelled! No room types exist in the database");
        }
    }
}
