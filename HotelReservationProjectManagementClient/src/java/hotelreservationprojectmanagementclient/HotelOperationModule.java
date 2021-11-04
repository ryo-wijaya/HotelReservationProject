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
import ejb.session.stateless.HotelManagementBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.Room;
import entity.RoomType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.enumeration.RatePerNight;
import util.exceptions.RoomIsTiedToABookingDeletionException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
public class HotelOperationModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private BookingSessionBeanRemote bookingSessionBean;
    private RoomTypeSessionBeanRemote roomTypeSessionBean;
    private RoomSessionBeanRemote roomSessionBean;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;

    private Employee currentEmployee;

    public HotelOperationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, BookingSessionBeanRemote bookingSessionBean, RoomTypeSessionBeanRemote roomTypeSessionBean, RoomSessionBeanRemote roomSessionBean, RoomRateSessionBeanRemote roomRateSessionBeanRemote, Employee currentEmployee) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.bookingSessionBean = bookingSessionBean;
        this.roomTypeSessionBean = roomTypeSessionBean;
        this.roomSessionBean = roomSessionBean;
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
            System.out.println("You are currently logged in as an Operation Manager");
            System.out.println("1. Room Types");
            System.out.println("2. Rooms");
            System.out.println("3. Logout");
            System.out.print("Please select a sub-module to work with>");
            submoduleOption = sc.nextInt();
            int option;

            if (submoduleOption == 1) {
                boolean returnBack = false;
                while (!returnBack) {
                    System.out.println("You are now in the Room Type sub-module");
                    System.out.println("1. Create new Room Type");
                    System.out.println("2. Update a Room Type");
                    System.out.println("3. Delete a Room Type");
                    System.out.println("4. View All Room Types");
                    System.out.println("5. View Room Type details");
                    System.out.println("6. Back to sub-module page");
                    System.out.print("Please select an option>");
                    option = sc.nextInt();

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
                    System.out.println("You are now in the Room sub-module");
                    System.out.println("1. Create new Room");
                    System.out.println("2. Update a Room");
                    System.out.println("3. Delete a Room");
                    System.out.println("4. View All Rooms");
                    System.out.println("5. View Room Allocation Exception Report");
                    System.out.println("6. Back to sub-module page");
                    System.out.print("Please select an option>");
                    option = sc.nextInt();

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
            }
        }
    }

    private void runSalesMenu() {
        Scanner sc = new Scanner(System.in);
        boolean logout = false;
        int option;
        while (!logout) {
            System.out.println("You are currently logged in as an Sales Manager");
            System.out.println("1. Create new Room Rate");
            System.out.println("2. Update a Room Rate");
            System.out.println("3. Delete a Room Rates");
            System.out.println("4. View all Room Rates");
            System.out.println("5. View Room Rates details");
            System.out.print("Please select an option>");
            option = sc.nextInt();

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
                    this.viewAllRoomRates(sc);
                    break;
                case 5:
                    this.viewRoomRateDetails(sc);
                    break;
                case 6:
                    logout = true;
                    break;
                default:
                    System.out.println("Please input a valid choice");
            }
        }
    }

    //NOTE: Development of these methods will follow Room Type -> Room Rate -> Room
    private void createNewRoomType(Scanner sc) {
        System.out.println("You are now creating a Room Type");
        System.out.println("Please enter a Room Type name: ");
        String name = sc.nextLine();
        System.out.println("Please enter select a room Ranking: ");
        Integer newranking = sc.nextInt();
        RoomType newRoomType = new RoomType(name, newranking);
        Integer ranking = newranking;
        List<RoomType> roomTypeBellowRanking = roomTypeSessionBean.getRoomTypeBelowRanking(newranking);
        for (RoomType updateroomType : roomTypeBellowRanking) {
            try {
                roomTypeSessionBean.updateRoomType(updateroomType.getRoomTypeId(), updateroomType.getRoomName(), ranking + 1);
            } catch (RoomTypeNotFoundException ex) {
                System.out.println("room type not found!");
            }
            ranking++;
        }
        System.out.println("Please enter select publish rate: ");
        Double publishRate = sc.nextDouble();
        System.out.println("Please enter select normal rate: ");
        Double normalRate = sc.nextDouble();
        System.out.println("Please enter select peak rate: ");
        Double peakRate = sc.nextDouble();
        System.out.println("Please enter select promotion rate: ");
        Double promotionRate = sc.nextDouble();
        roomTypeSessionBean.createNewRoomType(newRoomType);
    }

    private void updateARoomType(Scanner sc) {
        System.out.println("You are now updating a Room Type");
        System.out.println("Select a Room Type to update");

        String roomTypeName = sc.nextLine().trim();
        RoomType roomType = roomTypeSessionBean.getRoomTypeByName(roomTypeName);
        System.out.println("Select a New Room Type name");
        String newRoomTypeName = sc.nextLine().trim();
        System.out.println("Select a New Room Type ranking");
        Integer newRoomTypeRanking = sc.nextInt();
        Integer ranking = newRoomTypeRanking;
        List<RoomType> roomTypeBellowRanking = roomTypeSessionBean.getRoomTypeBelowRanking(newRoomTypeRanking);
        for (RoomType updateroomType : roomTypeBellowRanking) {
            try {
                roomTypeSessionBean.updateRoomType(updateroomType.getRoomTypeId(), updateroomType.getRoomName(), ranking + 1);
            } catch (RoomTypeNotFoundException ex) {
                System.out.println("room type not found!");
            }
            ranking++;
        }
        try {
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
        System.out.println("You are now creating a Room");
        System.out.println("Please enter a floor");
        String floor = sc.nextLine().trim();
        System.out.println("Please enter a room number");
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
        for (int i = 0; i < RatePerNight.values().length; i++) {
            System.out.println((i + 1) + ". " + RatePerNight.values()[i]);
        }

        RatePerNight rate;
        double price;
        String startDate;
        String endDate;

        System.out.print("Select a rate from 1-4>");
        int option = sc.nextInt();
        while (true) {
            if (option == 1) {
                rate = RatePerNight.PUBLISHRATE;
                break;
            } else if (option == 2) {
                rate = RatePerNight.PEAKRATE;
                break;
            } else if (option == 3) {
                rate = RatePerNight.NORMALRATE;
                break;
            } else if (option == 4) {
                rate = RatePerNight.PROMOTIONRATE;
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
        System.out.print("Input a price>");
        price = sc.nextDouble();
        System.out.print("Input Start Date in dd/mm/yyyy (with the slashes)>");
        startDate = sc.next();
        System.out.print("Input End Date in dd/mm/yyyy (with the slashes)>");
        endDate = sc.next();

        //extracting date
        int[] depDate = Arrays.stream(departureDate.split("/")).mapToInt(Integer::parseInt).toArray();
        int[] retDate = Arrays.stream(returnDate.split("/")).mapToInt(Integer::parseInt).toArray();
        departureDateObj = new Date(depDate[3], depDate[2], depDate[1]);
        returnDateObj = new Date(retDate[3], retDate[2], retDate[1]);
    }

    private void updateARoomRate(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void deleteARoomRate(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void viewAllRoomRates(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void viewRoomRateDetails(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
