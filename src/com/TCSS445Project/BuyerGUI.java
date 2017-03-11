package com.TCSS445Project;

//import static BidderGUI.COLUMNNUMBERS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.*;
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
    private static final String NP_ITEM_ADD_FORM = "NP Item Add Form";

    private static final String SELECT_STORE = "Enter a Store ID#";
    private static final String SELECT_ITEM = "Enter an Item ID#";

    private User user;
    private DB db;

    final static int COLUMNNUMBERS = 8;
    final static int ITEM_COL_NUMS = 4;

    final static String[] SELLERCOLUMNNAMES = {"Store ID#",
                                         "Store Name",
                                         "Contact Email",
    };

    final static String[] ITEMCOLUMNNAMES = {"ID #",
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
    private JPanel myMainButtonsPane; // Stores ALL buttons
    private JPanel myInputPane; // Stores Input prompt and textfield
    private JScrollPane scrollPane;
    private JScrollPane itemScrollPane;

    private JTextArea NO_ITEM_WELCOME;

    private JTable mySellerTable;
    private JTable mySellerItemTable;

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
        myOptionButtons = new ButtonBuilder(new String[] {"View All Stores", "Logout"});
        myCartButtons = new ButtonBuilder(new String[] {"Visit Storefront", "Add to Cart", "Clear Input"});


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
        myOptionButtons.getButton(1).addActionListener(new LogOut());
        myCartButtons.getButton(0).addActionListener(new ViewStorefront());
        myCartButtons.getButton(2).addActionListener(new ClearInput());

        BuyerWelcomeScreen();

        myLocalContainer.setLayout(myLocalCLayout);
        myLocalContainer.add(myViewSellersScreen, BuyerPANEL);
        myLocalContainer.add(myViewSellerItemsScreen, NP_Storefront_VIEW_SCREEN);

        myLocalCLayout.show(myLocalContainer, BuyerPANEL); // Inital Screen


        myMainScreen.add(myLocalContainer, BorderLayout.CENTER);

    }

    private void setupButtonPane() {
        myOptionButtons.buildButtons();
        myCartButtons.buildButtons();
        myCartButtons.getButton(1).setVisible(false);
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

        mySellerItemTable = new JTable(data, ITEMCOLUMNNAMES);
        itemScrollPane = new JScrollPane(mySellerItemTable);
        //myViewItemsScreen.setLayout(new BorderLayout());
        myViewSellerItemsScreen.add(itemScrollPane, BorderLayout.CENTER);
        mySellerItemTable.repaint();
        itemScrollPane.repaint();
        return (mySellerItems.size() > 0);
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
                        "Please enter a valid Seller ID# (1 or higher)");
            }

        }
    }

    class ViewSellersList implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ViewSellersScreen();
            myOptionButtons.getButton(0).setEnabled(false);
            myCartButtons.getButton(0).setVisible(true);
            myCartButtons.getButton(1).setVisible(false);
            myInputHint.setText(SELECT_STORE);
            myInputField.setValue(null);
            myViewSellerItemsScreen.remove(itemScrollPane);
            myLocalCLayout.show(myLocalContainer, BuyerPANEL);
//            myLocalContainer.repaint();
        }
    }

    class ClearInput implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            myInputField.setValue(null);
        }
    }

//    class SubmitItem implements ActionListener
//    {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            boolean problem = false;
//            if (myInputField.getText().matches(""))
//            {
//                JOptionPane.showMessageDialog(myMainScreen,
//                        "Please enter a name for this item.",
//                        "No name",
//                        JOptionPane.ERROR_MESSAGE);
//                problem = true;
//            }
//            if (myItemDesc.getText().matches(""))
//            {
//                JOptionPane.showMessageDialog(myMainScreen,
//                        "Please give this item a description.",
//                        "No description",
//                        JOptionPane.ERROR_MESSAGE);
//                problem = true;
//            }
//            try {
//                myItemPrice.commitEdit();
//            } catch (ParseException ex) {
//                //Logger.getLogger(SellerGUI.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            if ((double) myItemPrice.getValue() <= 0)
//            {
//                JOptionPane.showMessageDialog(myMainScreen,
//                        "Please enter a starting bid for this item.",
//                        "No starting bid",
//                        JOptionPane.ERROR_MESSAGE);
//                problem = true;
//            }
//            //System.out.println((double) myItemPrice.getValue());
//            if (myItemCnd.getSelectedIndex() == 0 || myItemCnd.getSelectedIndex() == 1)
//            {
//                JOptionPane.showMessageDialog(myMainScreen,
//                        "Please select a condition level for this item.",
//                        "Condition issue",
//                        JOptionPane.ERROR_MESSAGE);
//                problem = true;
//            }
//            if (myItemSize.getSelectedIndex() == 0 || myItemSize.getSelectedIndex() == 1)
//            {
//                JOptionPane.showMessageDialog(myMainScreen,
//                        "Please select an approximate size for this item.",
//                        "Size issue",
//                        JOptionPane.ERROR_MESSAGE);
//                problem = true;
//            }
//
//            if (!problem) {
//                String itemName = myInputField.getText();
//                String itemDesc = myItemDesc.getText();
//                int itemQty = (int) myItemQty.getValue();
//                double itemPrice = (double) myItemPrice.getValue();
//                String itemCnd = (String) myItemCnd.getSelectedItem();
//                String itemSize = (String) myItemSize.getSelectedItem();
//                String itemComment = myItemComments.getText();
//
//                Item item = new Item(0, user.getUserID(), itemName, itemDesc,
//                        itemQty, itemPrice, itemCnd, itemSize, itemComment);
//                db.start();
//                boolean noProblem = db.addItem(item);
//                db.close();
//                if (noProblem)
//                    JOptionPane.showMessageDialog(myMainScreen,
//                            "Your item has been successfully entered into our system.\nYou may continue entering items or click View Storefront to review your item list.",
//                            "Success!",JOptionPane.PLAIN_MESSAGE);
//                else {
//                    JOptionPane.showMessageDialog(myMainScreen,
//                            "There seems to have been a problem.",
//                            "Failure!",JOptionPane.PLAIN_MESSAGE);
//                }
//            }
//        }
//    }
}
