public void start() {
    System.out.println("🏥 WELCOME TO MEDICINE STOCK & EXPIRY TRACKER 🏥");

    // Check if database is connected
    if (!database.isConnected()) {
        System.out.println("❌ WARNING: Not connected to database!");
        System.out.println("   Some features will not work properly.");
        System.out.println("   Press Enter to continue anyway...");
        scanner.nextLine();
    }

    while (true) {
        showMenu();
        int choice = getIntInput("Choose option: ");

        switch (choice) {
            case 1:
                addMedicine();
                break;
            case 2:
                viewAllMedicines();
                break;
            case 3:
                checkExpiryAlerts();
                break;
            case 4:
                System.out.println("👋 Thank you for using Medicine Tracker. Goodbye!");
                database.close();
                return;
            default:
                System.out.println("❌ Invalid option! Please choose 1-4.");
        }
    }
}