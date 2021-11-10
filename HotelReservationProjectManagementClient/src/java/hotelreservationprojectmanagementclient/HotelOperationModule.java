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
import java.util.logging.Level;
import java.util.logging.Logger;
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
        if (currentEmployee.geteRole() == EmployeeRole.OPERATIONMANAGER) {
            this.runOperationsMenu();
        } else if (currentEmployee.geteRole() == EmployeeRole.SALESMANAGER) {
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
                            this.viewAllRoomTypes();
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
                            this.createNewRoom(sc);
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
            System.out.println("6. Exit");
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
            roomTypeSessionBeanRemote.createNewRoomType(newRoomType);

        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Next Higher Room Type is not found!");
        }
    }

    private void updateARoomType(Scanner sc) {
        System.out.println("\n-You are now updating a Room Type-");
        System.out.println("----------------------------------\n");

        try {
            RoomType roomType = this.viewRoomTypeDetails(sc);
            System.out.println("\nYou are now updating the Room Type details for the above Room Type\n");

            while (true) {
                int option;
                System.out.println("1. Change Room Type name");
                System.out.println("2. Change Room Type next Higher Room Type");
                System.out.println("3. Change Room Type description");
                System.out.println("4. Change Room Type size");
                System.out.println("5. Change Room Type amount of beds");
                System.out.println("6. Change Room Type capacity");
                System.out.println("7. Add amenity to Room Type");
                System.out.println("8. Remove amenity from Room Type");
                System.out.println("9. Exit");
                System.out.print("Please enter a numeric option from 1-9");

                try {
                    option = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException ex) {
                    option = 404;
                }

                if (option == 1) {
                    System.out.print("Enter a new name for this Room Type>");
                    String newName = sc.nextLine().trim();
                    roomTypeSessionBeanRemote.changeNextHigherRoomTypeNameForAChangedRoomTypeName(roomType.getRoomName(), newName);
                    System.out.println("Successfully updated!");

                } else if (option == 2) {
                    System.out.print("Enter a next Higher Room Type for this Room Type>");
                    String newHigherType = sc.nextLine().trim();
                    RoomType nextHigherExist = roomTypeSessionBeanRemote.getRoomTypeByName(newHigherType);
                    roomType.setNextHigherRoomType(newHigherType);
                    roomTypeSessionBeanRemote.updateRoomType(roomType);
                    System.out.println("Successfully updated!");

                } else if (option == 3) {
                    System.out.print("Enter a new description for this Room Type>");
                    String newDescription = sc.nextLine().trim();
                    roomType.setDescription(newDescription);
                    roomTypeSessionBeanRemote.updateRoomType(roomType);
                    System.out.println("Successfully updated!");

                } else if (option == 4) {
                    System.out.print("Enter a new size for this Room Type>");
                    String newSize = sc.nextLine().trim();
                    roomType.setRoomSize(newSize);
                    roomTypeSessionBeanRemote.updateRoomType(roomType);
                    System.out.println("Successfully updated!");

                } else if (option == 5) {
                    int newBeds = 0;
                    System.out.print("Enter a new amount for the number of beds in this Room Type>");
                    while (newBeds != 404) {
                        try {
                            newBeds = Integer.parseInt(sc.nextLine().trim());
                        } catch (NumberFormatException ex) {
                            System.out.println("Please enter a valid Integer!");
                            newBeds = 404;
                        }
                    }
                    roomType.setBeds(newBeds);
                    roomTypeSessionBeanRemote.updateRoomType(roomType);
                    System.out.println("Successfully updated!");

                } else if (option == 6) {
                    int newCapacity = 0;
                    System.out.print("Enter a new amount for the capacity of this Room Type>");
                    while (newCapacity != 404) {
                        try {
                            newCapacity = Integer.parseInt(sc.nextLine().trim());
                        } catch (NumberFormatException ex) {
                            System.out.println("Please enter a valid Integer!");
                            newCapacity = 404;
                        }
                    }
                    roomType.setCapacity(newCapacity);
                    roomTypeSessionBeanRemote.updateRoomType(roomType);
                    System.out.println("Successfully updated!");

                } else if (option == 7) {
                    System.out.print("Enter a new amenity for this Room Type>");
                    String newAmenity = sc.nextLine().trim();
                    roomType.getAmenities().add(newAmenity);
                    roomTypeSessionBeanRemote.updateRoomType(roomType);
                    System.out.println("Successfully updated!");

                } else if (option == 8) {
                    System.out.print("Type in the amenity of the Room Type you wish to delete>");
                    String amenityToDelete = sc.nextLine().trim();
                    if (roomType.getAmenities().contains(amenityToDelete)) {
                        roomType.getAmenities().remove(amenityToDelete);
                        roomTypeSessionBeanRemote.updateRoomType(roomType);
                        System.out.println("Successfully updated!");
                    } else {
                        System.out.println("That amenity does not belong to this Room Type!");
                    }

                } else if (option == 9) {
                    break;

                } else {
                    System.out.println("Please enter a valid option!");
                }
            }
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("room type not found!");
        }
    }

    private void deleteARoomType(Scanner sc) {
        System.out.println("\n-You are now deleting a Room Type-");
        System.out.println("----------------------------------\n");
        try {
            RoomType roomType = viewRoomTypeDetails(sc);
            if (!roomType.getEnabled()) {
                System.out.println("Room Type is already deleted!");
                return;
            }
            System.out.print("Enter 'Y' to confirm deletion>");
            String response = sc.nextLine().trim();
            if ("Y".equals(response)) {
                roomTypeSessionBeanRemote.deleteRoomType(roomType.getRoomTypeId());
                System.out.println("Room Type successfully deleted!");
            } else {
                System.out.println("Deletion cancelled!");
            }
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("invalid room type name");
        }

    }

    private void viewAllRoomTypes() {
        try {
            System.out.println("\n-You are now viewing all Room Types-");
            System.out.println("------------------------------------");
            List<RoomType> listOfRoomTypes = roomTypeSessionBeanRemote.retrieveRoomTypes();
            for (int i = 1; i <= listOfRoomTypes.size(); i++) {
                System.out.println(i + ". " + listOfRoomTypes.get(i - 1).getRoomName());
            }
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("No Rooms Exist in the Database!");
        }
    }

    private RoomType viewRoomTypeDetails(Scanner sc) {
        try {
            viewAllRoomTypes();
            System.out.println("\n-You are now viewing a Room Type detail of a Room Type-");
            System.out.println("-------------------------------------------------------\n");
            System.out.print("Enter a Room Type name>");
            String typeName = sc.nextLine().trim();
            RoomType roomType = roomTypeSessionBeanRemote.getRoomTypeByName(typeName);
            System.out.println("Room Type Name: " + roomType.getRoomName());
            System.out.println("Next Higher Room Type: " + roomType.getNextHigherRoomType());
            System.out.println("Description: " + roomType.getDescription());
            System.out.println("Room Size: " + roomType.getRoomSize());
            System.out.println("Beds: " + roomType.getBeds());
            System.out.println("Capacity: " + roomType.getCapacity());
            for (String amenities : roomType.getAmenities()) {
                System.out.println("Amentity: " + amenities);
            }

            if (roomType.getEnabled()) {
                System.out.println("Status: Enabled");
            } else {
                System.out.println("Status: Disabled");
            }

            return roomType;
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("A Room Type does not exist for that name!");
            return null;
        }
    }

    private void createNewRoom(Scanner sc) {
        try {
            System.out.println("\n-You are now creating a new Room-");
            System.out.println("---------------------------------\n");
            System.out.print("Please enter a room number>");
            String roomNumber = sc.nextLine().trim();
            this.viewAllRoomTypes();
            System.out.println("Please enter a room type");
            String roomTypeName = sc.nextLine();
            RoomType roomType = roomTypeSessionBeanRemote.getRoomTypeByName(roomTypeName);
            Room room = new Room(roomNumber);
            roomSessionBeanRemote.createNewRoom(room, roomType.getRoomTypeId());
            System.out.println("Room Successfully created");

        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Invalid Room Type");
        }

    }

    private void updateARoom(Scanner sc) {
        System.out.println("\n-You are now updating a Room-");
        System.out.println("-----------------------------\n");
        System.out.println("");
        String roomNumber = sc.nextLine().trim();
        try {
            Room room = roomSessionBeanRemote.getRoomByRoomNumber(roomNumber);
            int option;
            while (true) {
                System.out.println("1. Update Room Number");
                System.out.println("2. Update Room status");
                System.out.println("3. Back");
                System.out.print("Enter your choice>");

                try {
                    option = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException ex) {
                    option = 404;
                }

                if (option == 1) {
                    System.out.print("Please enter a new room number>");
                    String newRoomNumber = sc.nextLine().trim();
                    room.setRoomNumber(newRoomNumber);
                    roomSessionBeanRemote.updateRoom(room);
                    System.out.println("Successfully updated!");

                } else if (option == 2) {
                    if (room.getRoomStatus()) {
                        System.out.println("Room status changed from in-use to not-in-use");
                        room.setRoomStatus(true);
                    } else {
                        System.out.println("Room changed from not-in-use to in-use");
                        room.setRoomStatus(false);
                    }
                    roomSessionBeanRemote.updateRoom(room);
                    System.out.println("Successfully updated!");

                } else if (option == 3) {
                    break;
                } else {
                    System.out.println("Please input a valid option!");
                }
            }
        } catch (RoomNotFoundException ex) {
            System.out.println("Invalid room number!");
        }
    }

    private void deleteARoom(Scanner sc) {
        System.out.println("\n-You are now deleting a Room-");
        System.out.println("-----------------------------\n");
        System.out.print("Please enter a Room Number>");
        String roomNumber = sc.nextLine().trim();
        try {
            roomSessionBeanRemote.deleteRoomByRoomNumber(roomNumber);
            System.out.println("Room successfully deleted!");
        } catch (RoomNotFoundException ex) {
            System.out.println("Room not found!");
        } catch (RoomIsTiedToABookingDeletionException ex) {
            System.out.println("Room is currently tied to a booking!");
        }
    }

    private void viewAllRooms(Scanner sc) {
        try {
            System.out.println("\n-You are now viewing all Rooms-");
            System.out.println("-------------------------------\n");
            List<Room> rooms = roomSessionBeanRemote.retrieveRooms();
            
            if (rooms.isEmpty()) {
                System.out.println("No Rooms Found!");
                return;
            }
            
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
            System.out.println("No Rooms Found");
        }
    }

    private void viewRoomAllocationExceptionReport(Scanner sc) {
        //Booking probably needs another attribute - exceptionType : Enumeration, where exceptionType = {NONE, UPGRADED, NOUPGRADE}
        //All Bookings are defaulted to exceptionType = NONE
        //If during allocation a booking fails we change the type and we check for the type in this method

        System.out.println("\n-You are now viewing a Room Allocation Exception Report-");
        System.out.println("--------------------------------------------------------\n");
        System.out.println("Exception Type 1: List of Booking IDs where a room was upgraded\n");
        //query bookings thru session bean
        System.out.println("Exception Type 2: List of Booking IDs where no upgrade is available\n");
        //query bookings thru session bean
    }

    private void createNewRoomRate(Scanner sc) {
        RateType rate;
        double price = 0;
        String startDateString;
        String endDateString;
        Date startDate;
        Date endDate;
        String roomTypeName;
        RoomType roomType;

        System.out.println("\n-You are now creating a new Room Rate-");
        System.out.println("--------------------------------------\n");

        System.out.print("Enter the Room Type to associate this new Room Rate to>");
        roomTypeName = sc.nextLine().trim();

        try {
            roomType = roomTypeSessionBeanRemote.getRoomTypeByName(roomTypeName);
            boolean cannotModify = false;
            while (true) {
                int roomRateType;
                for (int i = 0; i < RateType.values().length; i++) {
                    System.out.println((i + 1) + ". " + RateType.values()[i]);
                }
                System.out.print("Select a rate from 1-4>");

                try {
                    roomRateType = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException ex) {
                    roomRateType = 404;
                }

                if (roomRateType == 1) {
                    for (RoomRate rr : roomType.getListOfRoomRates()) {
                        if (rr.getRateType() == PUBLISHRATE) {
                            cannotModify = true;
                        }
                    }
                    rate = RateType.PUBLISHRATE;
                    break;
                } else if (roomRateType == 2) {
                    rate = RateType.PEAKRATE;
                    break;
                } else if (roomRateType == 3) {
                    for (RoomRate rr : roomType.getListOfRoomRates()) {
                        if (rr.getRateType() == NORMALRATE) {
                            cannotModify = true;
                        }
                    }
                    rate = RateType.NORMALRATE;
                    break;
                } else if (roomRateType == 4) {
                    rate = RateType.PROMOTIONRATE;
                    break;
                } else {
                    System.out.println("Invalid choice!");
                }
            }

            if (!cannotModify) { //the room rate already has a normal or published rate and ur trying to add it
                RoomRate newRate;

                while (price != 404) {
                    try {
                        System.out.print("Input a price>");
                        price = Double.parseDouble(sc.nextLine().trim());
                    } catch (NumberFormatException ex) {
                        System.out.println("Please input a valid price!");
                        price = 404;
                    }
                }

                if (rate != PUBLISHRATE && rate != NORMALRATE) { //asking for date only if its a promotionrate or peakrate
                    System.out.print("Input Start Date in dd/mm/yyyy (with the slashes)>");
                    startDateString = sc.next();
                    System.out.print("Input End Date in dd/mm/yyyy (with the slashes)>");
                    endDateString = sc.next();

                    int[] sDate = Arrays.stream(startDateString.split("/")).mapToInt(Integer::parseInt).toArray();
                    int[] eDate = Arrays.stream(endDateString.split("/")).mapToInt(Integer::parseInt).toArray();
                    startDate = new Date(sDate[3], sDate[2], sDate[1]);
                    endDate = new Date(eDate[3], eDate[2], eDate[1]);
                    newRate = new RoomRate(rate, price, startDate, endDate);
                } else {
                    newRate = new RoomRate(rate, price);
                }

                roomRateSessionBeanRemote.createNewRoomRate(newRate, roomType.getRoomTypeId());

            } else {
                System.out.println("This Room Type already has either a PUBLISHEDRATE or a NORMALRATE");
            }

        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Room Type not found!");
        } catch (FailedToCreateRoomRateException ex) {
            System.out.println("Room Rate already exists in the Database!");
        }
    }

    private void updateARoomRate(Scanner sc) {
        System.out.println("\n-You are now updating a Room Rate-");
        System.out.println("----------------------------------\n");
        String roomNumber = sc.nextLine().trim();

        List<RoomRate> roomRate = this.viewRoomRateDetails(sc);
        if (roomRate.isEmpty()) {
            return;
        }

        int roomTypeChoice;

        for (int i = 0; i < roomRate.size(); i++) {
            System.out.println((i + 1) + ". Rate Type: " + roomRate.get(i).getRateType() + " Rate Per Night: " + roomRate.get(i).getPrice() + " Start Date: "
                    + roomRate.get(i).getStartDate() + " End Date: " + roomRate.get(i).getEndDate());
        }
        System.out.print("Please enter a Room Rate to update(numeric)>");
        try {
            roomTypeChoice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Update Cancelled! Invalid option!");
            return;
        }

        //setting a scenario
        RoomRate roomRateToEdit = roomRate.get(roomTypeChoice);
        int scenario;
        if (roomRateToEdit.getRateType() == NORMALRATE || roomRateToEdit.getRateType() == PUBLISHRATE) {
            scenario = 1;
        } else {
            scenario = 2;
        }

        boolean moreToEdit = true;
        while (moreToEdit) {
            System.out.println("\nWhat to update?\n");
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
                roomRateSessionBeanRemote.updateRoomRate(roomRateToEdit);
                System.out.println("Successfully Updated");

            } else if (editOption == 2) { //editing a price
                System.out.println("The old rate is: $" + roomRateToEdit.getPrice());
                double newPrice = sc.nextDouble();
                roomRateToEdit.setPrice(newPrice);
                roomRateSessionBeanRemote.updateRoomRate(roomRateToEdit);
                System.out.println("Successfully Updated");

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
                roomRateSessionBeanRemote.updateRoomRate(roomRateToEdit);
                System.out.println("Successfully Updated");
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
    }

    private void deleteARoomRate(Scanner sc) {
        try {
            System.out.println("\n-You are now deleting a Room Rate-");
            System.out.println("--------------------------------------\n");
            List<RoomRate> roomRates = viewRoomRateDetails(sc);
            if (roomRates.size() > 1) {
                System.out.println("Please select room rate to delete. (1 - " + roomRates.size() + ")");
            }
            Integer response = sc.nextInt();
            roomRateSessionBeanRemote.deleteRoomRate(roomRates.get(response - 1).getRoomRateId());

        } catch (RoomRateNotFoundException ex) {
            System.out.println("Invalid Room Rate!");
        }
    }

    private void viewAllRoomRates() {
        System.out.println("\n-Displaying a list of Room Rates-");
        System.out.println("---------------------------------\n");
        try {
            List<RoomRate> listOfRoomRates = roomRateSessionBeanRemote.retrieveRoomRates();
            for (RoomRate rr : listOfRoomRates) {
                System.out.println("Rate Type: " + rr.getRateType());
                System.out.println("Rate Per Night: " + rr.getPrice());
                System.out.println("Start Date: " + rr.getStartDate());
                System.out.println("End Date: " + rr.getEndDate());
                System.out.println("------------------------------------");
            }
        } catch (RoomRateNotFoundException ex) {
            System.out.println("No Room Rate Exists in the database!");
        }
    }

    private List<RoomRate> viewRoomRateDetails(Scanner sc) {

        System.out.println("\n-You are now viewing details of a Room Rate-");
        System.out.println("--------------------------------------------\n");
        System.out.println("Please enter Room Type name> ");
        String roomTypeName = sc.nextLine().trim();
        try {
            RoomType roomType = roomTypeSessionBeanRemote.getRoomTypeByName(roomTypeName);
            while (true) {
                int response = 0;
                System.out.println("Please enter Room Type's room rate (1: Publish Rate, 2: Normal Rate, 3: Peak Rate, 4: Promotion Rate, 5: Exit)");

                try {
                    response = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException ex) {
                    response = 404;
                }

                if (response == 1) {
                    List<RoomRate> roomRates = roomTypeSessionBeanRemote.getRoomRateByRoomNameAndRateType(roomTypeName, PUBLISHRATE);
                    if (!roomRates.isEmpty()) {
                        for (RoomRate roomRate : roomRates) {
                            System.out.println("Room Type Name: " + roomTypeName);
                            System.out.println("Rate Type: " + PUBLISHRATE);
                            System.out.println("Rate per night: " + roomRate.getPrice());
                        }
                        return roomRates;
                    } else {
                        System.out.println("Rate Type does not exists!");
                    }
                } else if (response == 2) {
                    List<RoomRate> roomRates = roomTypeSessionBeanRemote.getRoomRateByRoomNameAndRateType(roomTypeName, NORMALRATE);
                    if (!roomRates.isEmpty()) {
                        for (RoomRate roomRate : roomRates) {
                            System.out.println("Room Type Name: " + roomTypeName);
                            System.out.println("Rate Type: " + NORMALRATE);
                            System.out.println("Rate per night: " + roomRate.getPrice());
                        }
                        return roomRates;
                    } else {
                        System.out.println("Rate Type does not exists!");
                    }
                } else if (response == 3) {
                    List<RoomRate> roomRates = roomTypeSessionBeanRemote.getRoomRateByRoomNameAndRateType(roomTypeName, PEAKRATE);
                    if (!roomRates.isEmpty()) {
                        for (RoomRate roomRate : roomRates) {
                            System.out.println("Room Type Name: " + roomTypeName);
                            System.out.println("Rate Type: " + PEAKRATE);
                            System.out.println("Rate per night: " + roomRate.getPrice());
                            System.out.println("Start Date: " + roomRate.getStartDate());
                            System.out.println("End Date: " + roomRate.getEndDate());
                        }
                        return roomRates;
                    } else {
                        System.out.println("Rate Type does not exists!");
                    }
                } else if (response == 4) {
                    List<RoomRate> roomRates = roomTypeSessionBeanRemote.getRoomRateByRoomNameAndRateType(roomTypeName, PROMOTIONRATE);
                    if (!roomRates.isEmpty()) {
                        for (RoomRate roomRate : roomRates) {
                            System.out.println("Room Type Name: " + roomTypeName);
                            System.out.println("Rate Type: " + PROMOTIONRATE);
                            System.out.println("Rate per night: " + roomRate.getPrice());
                            System.out.println("Start Date: " + roomRate.getStartDate());
                            System.out.println("End Date: " + roomRate.getEndDate());
                        }
                        return roomRates;
                    } else {
                        System.out.println("Rate Type does not exists!");
                    }
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid Room Rate!");
                }
            }
            return new ArrayList<>();
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Operation cancelled! No room types exist in the database");
            return new ArrayList<>();
        }
    }
}
