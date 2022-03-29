# HotelReservationProject
School backend software engineering project in a project team of size 2

### Description
A CLI enterprise application created for a fictional company "Merlion Hotel" used to managed its hotel room inventory, price rates, room reservations and guests.
Company employees would need to perform login/logout, perform CRUD on users, rooms, room rates, as well as perform logically proper room allocation for guests reserving or checking-in to a room

The system will also (additional business rules):
* Adopt either one of 4 types of rates, depending on set business rules
* Ensure sufficient room inventory during the stipulated timeframe before accepting a booking
* Room allocation is done every 2am in the morning
* Automatically upgrade a guest in the event of overbooking (This is because room allocation is not pre-determined)
* Generate an exception report upon allocation errors
* Provide both an interface for in-person walk-in reservations and online reservations
* Provide an interface for partner organisations to login and perform searches and reservations

### Technologies used
* Jakarta EE Platform
* GlassFish application server (Oracle)
* SOAP (Simple Object Access Protocol) web services that separate client applications can use to call back-end methods made available (for partner organisations)

### How to run
Build and deploy project and run any of the java application clients
