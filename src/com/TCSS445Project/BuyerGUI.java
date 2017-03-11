package com.TCSS445Project;

//import static BidderGUI.COLUMNNUMBERS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;

/**
 * The GUI for users that are represented as Seller
 * organizations in the system.
 *
 * @author Jacob Ackerman
 * @version 12.1.2016.001A
 */

public class BuyerGUI {

    /*
     * These static Strings are the titles of each of the cards, must make a new one
     * for every new page you intend on making.
     */
    private final static String INPUTPANEL = "Login Page";
    private final static String BuyerCARD = "Buyer Welcome Card";
    private final static String BuyerPANEL = "Buyer Page";
    private final static String SellerREQUESTPANEL = "Seller Storefront Request Page";
    private static final String NP_CONFIRMATION_SCREEN = "NP Confirmation Screen";
    private static final String NP_Storefront_VIEW_SCREEN = "All Stores View";
    private static final String VIEW_CART = "Cart Screen";

    private static final String SELECT_STORE = "Enter a Store ID#";
    private static final String SELECT_ITEM = "Enter an Item ID#";

    private User user;
    private DB db;

    final static int COLUMNNUMBERS = 4;
    final static int ITEM_COL_NUMS = 4;
    final static int  CART_COL_NUMS  = 5;

    final static String[] SELLERCOLUMNNAMES = {"Store ID#",
                                         "Store Name",
                                         "Email",
                                            "Phonenumber",
    };

    final static String[] ITEMCOLUMNNAMES = {"Item ID #",
            "Item Name",
            "Condition",
            "Price",
    };

    final static String[] CARTCOLUMNNAMES = {"Item ID #",
            "Seller ID #",
            "Item Name",
            "Condition",
            "Price",
    };

    /*
     * Local container is a JPanel with CardLayout that will hold the various different JPanels.
     * This JPanel container will be added to the myMainScreen CENTER and will change the views
     * while retaining the buttons along the bottom.
     */
    private JPanel myLocalContainer;
    private CardLayout myLocalCLayout;

    /*
     * These containers and CardLayout are from the main GUI and are to be used when logging out
     * and when first entering this GUI only.
     */
    private JPanel myMainContainer;
    private CardLayout myMainCLayout;

    private JPanel myMainScreen;	//Contains myLocalContainer in BorderLayout.CENTER, myOptionButtons stay along the bottom
    private JPanel myViewSellersScreen;	//JPanel that should contain the various Welcome JTextAreas. To be added in myLocalContainer only.
    private JPanel myViewSellerItemsScreen;
    private JPanel myViewCartScreen;
    private JPanel myMainButtonsPane; // Stores ALL buttons
    private JPanel myInputPane; // Stores Input prompt and textfield
    private JScrollPane scrollPane;
    private JScrollPane itemScrollPane;
    private JScrollPane cartScrollPane;

    private JTextArea NO_ITEM_WELCOME;

    private JTable mySellerTable;
    private JTable mySellerItemTable;
    private JTable myCartItemTable;

    private JLabel myInputHint;
    private JFormattedTextField myInputField;

    private ButtonBuilder myOptionButtons;
    private ButtonBuilder myCartButtons;


    private int[] myDate;     //Used to capture the date the user picks on the calendar. 0 = year, 1 = month, 2 = day

    /**
     * Constructor for SellerGUI.
     * @param theUser is the Seller user.
     * @param theContainer is the JPanel passed in from the main GUI, allows this GUI to use the same JFrame.
     * @param theCLayout is the CardLayout from the main GUI, allows this GUI to use the same JFrame.
     */
    public BuyerGUI(User theUser, JPanel theContainer, CardLayout theCLayout) {
        user = theUser;
        db = new DB();
        myMainContainer = theContainer;
        myMainCLayout = theCLayout;
        myDate = new int[3];

        myLocalContainer = new JPanel();
        myLocalCLayout = new CardLayout();
        myMainScreen = new JPanel();
        myViewSellersScreen = new JPanel(new BorderLayout());
        myInputPane = new JPanel(new GridBagLayout());
        myInputHint = new JLabel(SELECT_STORE);
        myMainButtonsPane = new JPanel(new GridLayout(3,1));
        //CONFIRMATION_MESSAGE = new JTextArea();
        myViewSellerItemsScreen = new JPanel(new BorderLayout());
        myViewCartScreen = new JPanel(new BorderLayout());

        NO_ITEM_WELCOME = new JTextArea("Welcome, " + user.getName() + "\n"
                + "\nYou currently have no items in your storefront.\n"
                + "Please click \"Add item\" if you would like to add an item.");

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        myInputField = new JFormattedTextField(formatter);

        NO_ITEM_WELCOME.setEditable(false);


    }

    /**
     * This method creates the Seller GUI.
     *
     * Creates the buttons with ButtonBuilder.
     * Calls BuyerScreenController which creates the main screen, adds all panels to the local CardLayout.
     *
     * Once myMainScreen is made, adds it to the Main Container and Main CardLayout for use with the main JFrame.
     */
    public void start() {
        myOptionButtons = new ButtonBuilder(new String[] {"View All Stores", "My Cart", "Logout"});
        myCartButtons = new ButtonBuilder(new String[] {"Visit Storefront", "Add to Cart", "Remove from Cart", "Clear Input"});


        BuyerScreenController();

        myMainContainer.add(myMainScreen, BuyerCARD);
        myMainCLayout.show(myMainContainer, BuyerCARD);

    }



    /**
     * This is the main method that creates the structure for SellerGUI.
     *
     * myMainScreen is a JPanel that is always showing. Contains myOptionButtons in BorderLayout.SOUTH.
     * Contains myLocalContainer in BorderLayout.CENTER.
     *
     * myLocalContainer holds all of the different panels that will need to change in this GUI.
     * myLocalCLayout is used to swap between the different panels in myLocalContainer so that myMainScreen can
     * stay the same and allow the buttons to always be present.
     *
     * When making new JPanels, you MUST create a static String that represents the new JPanel,
     * and you must ONLY add the new JPanel to myLocalContainer.
     *
     * To Add a JPanel to myLocalContainer,
     * myLocalContainer.add(XXXX, YYYY)		XXXX is the variable for the JPanel
     * 										YYYY is the static String created to describe the panel.
     *
     * For ActionListeners, to switch to a specific JPanel, you must call
     * myLocalCLayout.show(myLocalContainer, XXXXX)		XXXXX is the static String you created to describe the panel.
     */
    private void BuyerScreenController() {
        myMainScreen.setLayout(new BorderLayout());
        setupButtonPane();

        myMainScreen.add(myMainButtonsPane, BorderLayout.SOUTH);
        myOptionButtons.getButton(0).setEnabled(false);
//        myFrame.add(myMainScreen, BorderLayout.SOUTH);
//        myOptionButtons.getButton(0)
        myOptionButtons.getButton(0).addActionListener(new ViewSellersList());
        myOptionButtons.getButton(1).addActionListener(new ViewCartList());
        myOptionButtons.getButton(2).addActionListener(new LogOut());
        myCartButtons.getButton(0).addActionListener(new ViewStorefront());
        myCartButtons.getButton(1).addActionListener(new AddToCart());
        myCartButtons.getButton(2).addActionListener(new RemoveFromCart());
        myCartButtons.getButton(3).addActionListener(new ClearInput());

        BuyerWelcomeScreen();

        myLocalContainer.setLayout(myLocalCLayout);
        myLocalContainer.add(myViewSellersScreen, BuyerPANEL);
        myLocalContainer.add(myViewSellerItemsScreen, NP_Storefront_VIEW_SCREEN);
        myLocalContainer.add(myViewCartScreen, VIEW_CART);

        myLocalCLayout.show(myLocalContainer, BuyerPANEL); // Inital Screen


        myMainScreen.add(myLocalContainer, BorderLayout.CENTER);

    }

    private void setupButtonPane() {
        myOptionButtons.buildButtons();
        myCartButtons.buildButtons();
        myCartButtons.getButton(1).setVisible(false);
        myCartButtons.getButton(2).setVisible(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        myInputPane.add(myInputHint, c);
        c.gridx = 0;
        c.gridy = 1;
        myInputField.setColumns(10);
        myInputPane.add(myInputField, c);
        c.gridx = 1;
        c.gridy = 1;

//        myInputPane.setMaximumSize(new Dimension(25, 25));
        myMainButtonsPane.add(myInputPane);
        myMainButtonsPane.add(myCartButtons);
        myMainButtonsPane.add(myOptionButtons);
    }

    /**
     * This method creates the JPanel which should contain the Welcome text areas.
     */
    private void BuyerWelcomeScreen() {
        //System.out.println(myNPO.getStorefront());
        //System.out.println("result: " + myCal.getStorefront(myNPO.getUserName()));
        //System.out.println(myNPO.getUserName());
//        if (myCal.getStorefront(myNPO.getUserName()) == null)
        JLabel viewingSellers = new JLabel("Viewing All Storefronts");
        viewingSellers.setFont(new Font(viewingSellers.getFont().getName(),
                                        viewingSellers.getFont().getStyle(),
                                        30));
        myViewSellersScreen.add(viewingSellers, BorderLayout.NORTH);
        myViewSellersScreen.add(NO_ITEM_WELCOME, BorderLayout.CENTER);
        ViewSellersScreen();
//        else
//        {
//            initializeHasStorefrontMessage();
//            myViewSellersScreen.add(HAS_Storefront_WELCOME, BorderLayout.CENTER);
//        }
    }

    private boolean ViewSellersScreen() {
        db.start();
        ArrayList<User> mySellers = db.getAllSellers();
        db.close();
        System.out.println(mySellers.size());
        Object[][] data = new Object[mySellers.size()][COLUMNNUMBERS];
        int sellerID = 1;
        //for (int k = 0; k < COLUMNNUMBERS; k++) {
        //	data[0][k] = COLUMNNAMES[k];
        //}
        for (User i : mySellers) {
            for (int j = 0; j < COLUMNNUMBERS; j++) {
                if (j == 0) data[sellerID-1][j] = i.getUserID();
                if (j == 1) data[sellerID-1][j] = i.getUsername();
                if (j == 2) data[sellerID-1][j] = i.getEmail();
                if (j == 3) data[sellerID-1][j] = i.getPhoneNumber();
				/*
                                if (j == 4) {
					if (myBidder.viewBids().containsKey(i)) {
						data[itemID][j] = myBidder.viewBids().get(i);
					} else {
						data[itemID][j] = null;
					}
				}
                                */
            }
            sellerID++;
        }
        //if (myItems.size() == 0)
        //theButtons.getButton(2).setEnabled(false);

        mySellerTable = new JTable(data, SELLERCOLUMNNAMES);
//		myViewItemsScreen.setLayout(new BorderLayout());
//		myViewItemsScreen.add(mySellerTable, BorderLayout.CENTER);

        scrollPane = new JScrollPane(mySellerTable);
        //myViewItemsScreen.setLayout(new BorderLayout());
        myViewSellersScreen.add(scrollPane, BorderLayout.CENTER);
        mySellerTable.repaint();
        scrollPane.repaint();
        return (mySellers.size() > 0);
    }

    private boolean ViewSellerItemsScreen(int theSellerID) {
        db.start();
        ArrayList<Item> mySellerItems = db.getMyStoreItems(theSellerID);
        db.close();
        System.out.println(mySellerItems.size());
        Object[][] data = new Object[mySellerItems.size()][ITEM_COL_NUMS];
        int itemID = 1;
        //for (int k = 0; k < COLUMNNUMBERS; k++) {
        //	data[0][k] = COLUMNNAMES[k];
        //}
        for (Item i : mySellerItems) {
            for (int j = 0; j < ITEM_COL_NUMS; j++) {
                if (j == 0) data[itemID-1][j] = i.getItemID();
                if (j == 1) data[itemID-1][j] = i.getName();
                if (j == 2) data[itemID-1][j] = i.getConditionType();
                if (j == 3) data[itemID-1][j] = "$" + i.getPrice() + "0";
            }
            itemID++;
        }
        //if (myItems.size() == 0)
        //theButtons.getButton(2).setEnabled(false);

        mySellerItemTable = new JTable(data, ITEMCOLUMNNAMES);
        itemScrollPane = new JScrollPane(mySellerItemTable);
        //myViewItemsScreen.setLayout(new BorderLayout());
        JLabel viewingSellerItems = new JLabel("Viewing Store Items");
        viewingSellerItems.setFont(new Font(viewingSellerItems.getFont().getName(),
                viewingSellerItems.getFont().getStyle(),
                30));
        myViewSellerItemsScreen.add(viewingSellerItems, BorderLayout.NORTH);
        myViewSellerItemsScreen.add(itemScrollPane, BorderLayout.CENTER);
        mySellerItemTable.repaint();
        itemScrollPane.repaint();
        return (mySellerItems.size() > 0);
    }

    private boolean ViewCartItemsScreen() {
        db.start();
        ArrayList<Item> myCartItems = db.getMyCartItems(user.getUserID());
        db.close();
        System.out.println(myCartItems.size());
        Object[][] data = new Object[myCartItems.size()][CART_COL_NUMS];
        int itemID = 1;
        //for (int k = 0; k < COLUMNNUMBERS; k++) {
        //	data[0][k] = COLUMNNAMES[k];
        //}
        for (Item i : myCartItems) {
            for (int j = 0; j < CART_COL_NUMS; j++) {
                if (j == 0) data[itemID-1][j] = i.getItemID();
                if (j == 1) data[itemID-1][j] = i.getSellerID();
                if (j == 2) data[itemID-1][j] = i.getName();
                if (j == 3) data[itemID-1][j] = i.getConditionType();
                if (j == 4) data[itemID-1][j] = "$" + i.getPrice() + "0";

				/*
                                if (j == 4) {
					if (myBidder.viewBids().containsKey(i)) {
						data[itemID][j] = myBidder.viewBids().get(i);
					} else {
						data[itemID][j] = null;
					}
				}
                                */
            }
            itemID++;
        }
        //if (myItems.size() == 0)
        //theButtons.getButton(2).setEnabled(false);

        myCartItemTable = new JTable(data, CARTCOLUMNNAMES);
        cartScrollPane = new JScrollPane(myCartItemTable);
        //myViewItemsScreen.setLayout(new BorderLayout());
        JLabel viewingSellerItems = new JLabel("My Cart");
        viewingSellerItems.setFont(new Font(viewingSellerItems.getFont().getName(),
                viewingSellerItems.getFont().getStyle(),
                30));
        myViewCartScreen.add(viewingSellerItems, BorderLayout.NORTH);
        myViewCartScreen.add(cartScrollPane, BorderLayout.CENTER);
        myCartItemTable.repaint();
        cartScrollPane.repaint();
        return (myCartItems.size() > 0);
    }

    private int validInput() {
        Number input = (Number) myInputField.getValue();
        if (input == null)  {
            return 0;
        } else {
            return input.intValue();
        }
    }

    class LogOut implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            myMainCLayout.show(myMainContainer, INPUTPANEL);
            myMainContainer.remove(myMainScreen);
        }
    }

    class ViewStorefront implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int inputValid = validInput();
            if (inputValid != 0) {
                ViewSellerItemsScreen(inputValid);
                myOptionButtons.getButton(0).setEnabled(true);
                myCartButtons.getButton(0).setVisible(false);
                myCartButtons.getButton(1).setVisible(true);
                myInputHint.setText(SELECT_ITEM);
                myInputField.setValue(null);
                myLocalCLayout.show(myLocalContainer, NP_Storefront_VIEW_SCREEN);
//                myLocalContainer.repaint();
            } else {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a valid Seller ID#");
            }

        }
    }

    class ViewSellersList implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ViewSellersScreen();
            myOptionButtons.getButton(0).setEnabled(false);
            myOptionButtons.getButton(1).setEnabled(true);
            myCartButtons.getButton(0).setVisible(true);
            myCartButtons.getButton(1).setVisible(false);
            myCartButtons.getButton(2).setVisible(false);

            myInputHint.setText(SELECT_STORE);
            myInputField.setValue(null);
//            myViewSellerItemsScreen.remove(itemScrollPane);
            myViewCartScreen.add(new ScrollPane(), BorderLayout.CENTER);
            myViewSellerItemsScreen.add(new ScrollPane(), BorderLayout.CENTER);
            myLocalCLayout.show(myLocalContainer, BuyerPANEL);
//            myLocalContainer.repaint();
        }
    }

    class ViewCartList implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ViewCartItemsScreen();
            myCartButtons.getButton(0).setVisible(false);
            myCartButtons.getButton(1).setVisible(true);
            myCartButtons.getButton(2).setVisible(true);
            myOptionButtons.getButton(0).setEnabled(true);
            myOptionButtons.getButton(1).setEnabled(false);
            myInputHint.setText(SELECT_STORE);
            myInputField.setValue(null);
//            myViewCartScreen.remove(cartScrollPane);
            myLocalCLayout.show(myLocalContainer, VIEW_CART);
//            myLocalContainer.repaint();
        }
    }

    class ClearInput implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            myInputField.setValue(null);
        }
    }

    class AddToCart implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int inputIsValid = validInput();
            if (inputIsValid != 0) {

                db.start();
                boolean success = db.addToCart(user.getUserID(), inputIsValid);
                db.close();
                if (success) {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Item added to cart!");
                    myInputField.setValue(null);
                } else {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Add to cart failed!");
                    myInputField.setValue(null);
                }
            } else {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a valid Item ID#");
            }
        }
    }

    class RemoveFromCart implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int inputIsValid = validInput();
            if (inputIsValid != 0) {
                db.start();
                boolean success = db.removeFromCart(user.getUserID(), inputIsValid);
                db.close();
                if (success) {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Item removed from cart!");
                    myInputField.setValue(null);
                } else {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Remove from cart failed!");
                    myInputField.setValue(null);
                }
            } else {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a valid Item ID#");
            }
        }
    }
}
