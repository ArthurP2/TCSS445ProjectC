package com.TCSS445Project;

//import static BidderGUI.COLUMNNUMBERS;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * The GUI for users that are represented as Seller
 * organizations in the system.
 *
 * @author Jacob Ackerman
 * @version 12.1.2016.001A
 */

public class SellerGUI {

    /*
     * These static Strings are the titles of each of the cards, must make a new one
     * for every new page you intend on making.
     */
    private final static String INPUTPANEL = "Login Page";
    private final static String SellerCARD = "Seller Welcome Card";
    private final static String SellerPANEL = "Seller Page";
    private final static String SellerREQUESTPANEL = "Seller Storefront Request Page";
    private final static String Seller_Storefront_FORM = "Seller Storefront Request Form";
    private static final String NP_CONFIRMATION_SCREEN = "NP Confirmation Screen";
    private static final String NP_Storefront_VIEW_SCREEN = "NP Storefront View";
    private static final String NP_ITEM_ADD_FORM = "NP Item Add Form";

    private User user;
    private DB db;

    final static int IDWIDTH = 20;
    final static int NAMEWIDTH = 100;
    final static int CONDITIONWIDTH = 30;
    final static int MINBIDWIDTH = 30;
    final static int MYBIDWIDTH = 30;
    final static int COLUMNNUMBERS = 4;

    final static String[] COLUMNNAMES = {"ID #",
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
    private JPanel myWelcomeScreen;	//JPanel that should contain the various Welcome JTextAreas. To be added in myLocalContainer only.
    private JPanel myRequestScreen;	//JPanel that contains the Submit Storefront Request form. To be added in myLocalContainer only.
    private JPanel myRequestCalendarScreen;	//JPanel that helps create myRequestScreen. To be added to myRequestScreen only.
    private JPanel myRequestFormScreen;
    private JPanel myConfirmation;
    private JPanel myViewStorefrontScreen;
    private JPanel myAddItemForm;
    private JScrollPane scrollPane;

    private JTextArea NO_ITEM_WELCOME;
    private JTextArea HAS_Storefront_WELCOME;
    private static final JTextArea Storefront_REQUEST_HELP = new JTextArea("These are the next 30 days starting from today."
            + "\nDays that are available will have a clickable button."
            + "\nPlease select an available day to continue.");
    private JTextArea CONFIRMATION_MESSAGE;

    private JTextField myStorefrontName;
    private JTextField myContactPerson;
    private JTextField myItemCount;
    private JTextField myDescription;
    private JTextField myComments;
    private JComboBox myStartHour;
    private JTable myItemTable;
    /*
    name = theName;
		donorName = "";
		description = "";
		quantity = 0;
		startingBid = 0;
		condition = "";
		size = "";
		comments = "";
    */

    private JTextField myItemName;
    private JTextField myItemDesc;
    private JSpinner myItemQty;
    private JSpinner myItemPrice;
    private JComboBox myItemCnd;
    private JComboBox myItemSize;
    private JTextField myItemComments;

    private ButtonBuilder myOptionButtons;
    private ButtonBuilder viewStorefrontButtons;

    private int[] myDate;     //Used to capture the date the user picks on the calendar. 0 = year, 1 = month, 2 = day

    /**
     * Constructor for SellerGUI.
     * @param theUser is the Seller user.
     * @param theContainer is the JPanel passed in from the main GUI, allows this GUI to use the same JFrame.
     * @param theCLayout is the CardLayout from the main GUI, allows this GUI to use the same JFrame.
     */
    public SellerGUI(User theUser, JPanel theContainer, CardLayout theCLayout) {
        user = theUser;
        db = new DB();
        myMainContainer = theContainer;
        myMainCLayout = theCLayout;
        myDate = new int[3];

        myLocalContainer = new JPanel();
        myLocalCLayout = new CardLayout();
        myMainScreen = new JPanel();
        myWelcomeScreen = new JPanel(new BorderLayout());
        myRequestScreen = new JPanel();
        myRequestCalendarScreen = new JPanel();
        myRequestFormScreen = new JPanel();
        viewStorefrontButtons = new ButtonBuilder(new String[] {"Cancel Storefront", "Add Item", "Remove Item"});
        //CONFIRMATION_MESSAGE = new JTextArea();
        myConfirmation = new JPanel();
        myViewStorefrontScreen = new JPanel();
        myAddItemForm = new JPanel();

        NO_ITEM_WELCOME = new JTextArea("Welcome, " + user.getName() + "\n"
                + "\nYou currently have no items in your storefront.\n"
                + "Please click \"Add item\" if you would like to add an item.");

        myItemName = new JTextField();
        myItemDesc = new JTextField();
        myItemQty = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        myItemPrice = new JSpinner(new SpinnerNumberModel(0, 0, 9999.99, 1));
        myItemCnd = new JComboBox(new String[] {"Select Condition", "-----------", "New", "Like new", "Good", "Fair", "Poor", "Bad"});
        myItemSize = new JComboBox(new String[] {"Select Size", "------------", "Tiny", "Small", "Medium", "Large", "Huge"});
        myItemComments = new JTextField();

        NO_ITEM_WELCOME.setEditable(false);

        myStorefrontName = new JTextField();
        myContactPerson = new JTextField();
        myItemCount = new JTextField();
        myDescription = new JTextField();
        myComments = new JTextField();
        myStartHour = new JComboBox(new String[] {"Select Time", "------------", "Midnight", "1am", "2am", "3am", "4am", "5am", "6am", "7am", "8am", "9am", "10am", "11am", "Noon", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm", "11pm"});
    }

    /**
     * This method creates the Seller GUI.
     *
     * Creates the buttons with ButtonBuilder.
     * Calls SellerScreenController which creates the main screen, adds all panels to the local CardLayout.
     *
     * Once myMainScreen is made, adds it to the Main Container and Main CardLayout for use with the main JFrame.
     */
    public void start() {
        myOptionButtons = new ButtonBuilder(new String[] {"Add Item", "View Storefront", "Logout"});


        SellerScreenController();

        myMainContainer.add(myMainScreen, SellerCARD);
        myMainCLayout.show(myMainContainer, SellerCARD);

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
    private void SellerScreenController() {
        myMainScreen.setLayout(new BorderLayout());
        myOptionButtons.buildButtons();
        myMainScreen.add(myOptionButtons, BorderLayout.SOUTH);
        myOptionButtons.getButton(1).setEnabled(false);
//        myFrame.add(myMainScreen, BorderLayout.SOUTH);
        myOptionButtons.getButton(0).addActionListener(new AddItemForm());
        myOptionButtons.getButton(1).addActionListener(new ViewStorefront());
        myOptionButtons.getButton(2).addActionListener(new LogOut());

        SellerWelcomeScreen();
        SellerStorefrontRequestScreen();


        initializeAddItemForm();


        myLocalContainer.setLayout(myLocalCLayout);
        myLocalContainer.add(myWelcomeScreen, SellerPANEL);
        myLocalContainer.add(myRequestScreen, SellerREQUESTPANEL);
        myLocalContainer.add(myRequestFormScreen, Seller_Storefront_FORM);
        myLocalContainer.add(myConfirmation, NP_CONFIRMATION_SCREEN);
        myLocalContainer.add(myViewStorefrontScreen, NP_Storefront_VIEW_SCREEN);
        myLocalContainer.add(myAddItemForm, NP_ITEM_ADD_FORM);

        myLocalCLayout.show(myLocalContainer, SellerPANEL); // Inital Screen


        myMainScreen.add(myLocalContainer, BorderLayout.CENTER);

    }

    /**
     * This method creates the JPanel which should contain the Welcome text areas.
     */
    private void SellerWelcomeScreen() {
        //System.out.println(myNPO.getStorefront());
        //System.out.println("result: " + myCal.getStorefront(myNPO.getUserName()));
        //System.out.println(myNPO.getUserName());
//        if (myCal.getStorefront(myNPO.getUserName()) == null)
         myWelcomeScreen.add(NO_ITEM_WELCOME, BorderLayout.CENTER);
         NPViewItemsScreen();
//        else
//        {
//            initializeHasStorefrontMessage();
//            myWelcomeScreen.add(HAS_Storefront_WELCOME, BorderLayout.CENTER);
//        }
    }



    private void initializeAddItemForm()
    {
        myAddItemForm.setLayout(new BorderLayout());
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        myAddItemForm.add(form, BorderLayout.CENTER);
        JLabel info = new JLabel("This is the item submission form.");
        myAddItemForm.add(info, BorderLayout.NORTH);
        GridBagConstraints c = new GridBagConstraints();
        JButton submitButton = new JButton("Submit Item");
        submitButton.addActionListener(new SubmitItem());

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        form.add(new JLabel("Item name: "), c);
        c.gridx = 0;
        c.gridy = 2;
        form.add(new JLabel("Description: "), c);
        c.gridx = 0;
        c.gridy = 3;
        form.add(new JLabel("Quantity: "), c);
        c.gridx = 0;
        c.gridy = 4;
        form.add(new JLabel("Price ($): "), c);
        c.gridx = 0;
        c.gridy = 5;
        form.add(new JLabel("Condition: "), c);
        c.gridx = 0;
        c.gridy = 6;
        form.add(new JLabel("Size: "), c);
        c.gridx = 0;
        c.gridy = 7;
        form.add(new JLabel("Comments: "), c);

        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 1;
        form.add(myItemName, c);

        c.gridx = 1;
        c.gridy = 2;
        form.add(myItemDesc, c);
        c.gridx = 1;
        c.gridy = 3;
        c.ipadx = 50;
        form.add(myItemQty, c);
        c.gridx = 1;
        c.gridy = 4;
        //c.ipadx = 100;
        form.add(myItemPrice, c);
        c.gridx = 1;
        c.gridy = 5;
        c.ipadx = 0;
        form.add(myItemCnd, c);
        c.gridx = 1;
        c.gridy = 6;
        form.add(myItemSize, c);
        c.gridx = 1;
        c.gridy = 7;
        c.ipadx = 200;
        form.add(myItemComments, c);

        c.gridx = 1;
        c.gridy = 8;
        c.ipadx = 0;
        c.ipady = 50;
        form.add(new JPanel(), c);
        c.gridx = 1;
        c.gridy = 9;
        c.ipady = 0;
        form.add(submitButton, c);
    }

    private boolean NPViewItemsScreen() {
        db.start();
        ArrayList<Item> myItems = db.getMyStoreItems(user.getUserID());
        db.close();
        System.out.println(myItems.size());
        Object[][] data = new Object[myItems.size()][COLUMNNUMBERS];
        int itemID = 1;
        //for (int k = 0; k < COLUMNNUMBERS; k++) {
        //	data[0][k] = COLUMNNAMES[k];
        //}
        for (Item i : myItems) {
            for (int j = 0; j < COLUMNNUMBERS; j++) {
                if (j == 0) data[itemID-1][j] = i.getItemID();
                if (j == 1) data[itemID-1][j] = i.getName();
                if (j == 2) data[itemID-1][j] = i.getConditionType();
                if (j == 3) data[itemID-1][j] = "$" + i.getPrice();
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

        myItemTable = new JTable(data, COLUMNNAMES);
//		myViewItemsScreen.setLayout(new BorderLayout());
//		myViewItemsScreen.add(myItemTable, BorderLayout.CENTER);

        scrollPane = new JScrollPane(myItemTable);
        //myViewItemsScreen.setLayout(new BorderLayout());
        myWelcomeScreen.add(scrollPane, BorderLayout.CENTER);
        myItemTable.repaint();
        scrollPane.repaint();
        return (myItems.size() > 0);
    }

    /**
     * This method creates the Storefront Request JPanel.
     * Puts the text area in BorderLayout.NORTH with the prompt,
     * Puts the calendar in BorderLayout.CENTER
     */
    private void SellerStorefrontRequestScreen()
    {
        myRequestScreen.setLayout(new BorderLayout());

        InitializeRequestScreen();
        myRequestScreen.add(Storefront_REQUEST_HELP, BorderLayout.NORTH);
        myRequestScreen.add(myRequestCalendarScreen, BorderLayout.CENTER);
        myRequestScreen.setVisible(true);
    }

    /**
     * Basically the same as what you wrote,
     * Instead of adding to myRequestScreen directly, it adds to a separate panel
     * so that myRequestScreen can be formatted properly
     */
    private void InitializeRequestScreen()
    {
        GridLayout gLayout = new GridLayout(0, 7);
        //gLayout.setColumns(7);
        //gLayout.setRows(6);
        myRequestCalendarScreen.setLayout(gLayout);
        Calendar cal = Calendar.getInstance();



        //ArrayList<Storefront> Storefronts = (ArrayList<Storefront>) myCal.getStorefronts();




    }






    class LogOut implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            myMainCLayout.show(myMainContainer, INPUTPANEL);
            myMainContainer.remove(myMainScreen);

        }
    }



    class ViewStorefront implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            myMainScreen.remove(scrollPane);
            NPViewItemsScreen();
            myOptionButtons.getButton(1).setEnabled(false);
            myLocalCLayout.show(myLocalContainer, SellerPANEL);


        }

    }

    class AddItemForm implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            myLocalCLayout.show(myLocalContainer, NP_ITEM_ADD_FORM);
            myOptionButtons.getButton(1).setEnabled(true);
        }

    }

    class SubmitItem implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean problem = false;
            if (myItemName.getText().matches(""))
            {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a name for this item.",
                        "No name",
                        JOptionPane.ERROR_MESSAGE);
                problem = true;
            }
            if (myItemDesc.getText().matches(""))
            {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please give this item a description.",
                        "No description",
                        JOptionPane.ERROR_MESSAGE);
                problem = true;
            }
            try {
                myItemPrice.commitEdit();
            } catch (ParseException ex) {
                //Logger.getLogger(SellerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if ((double) myItemPrice.getValue() <= 0)
            {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a starting bid for this item.",
                        "No starting bid",
                        JOptionPane.ERROR_MESSAGE);
                problem = true;
            }
            //System.out.println((double) myItemPrice.getValue());
            if (myItemCnd.getSelectedIndex() == 0 || myItemCnd.getSelectedIndex() == 1)
            {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please select a condition level for this item.",
                        "Condition issue",
                        JOptionPane.ERROR_MESSAGE);
                problem = true;
            }
            if (myItemSize.getSelectedIndex() == 0 || myItemSize.getSelectedIndex() == 1)
            {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please select an approximate size for this item.",
                        "Size issue",
                        JOptionPane.ERROR_MESSAGE);
                problem = true;
            }

            if (!problem) {
                String itemName = myItemName.getText();
                String itemDesc = myItemDesc.getText();
                int itemQty = (int) myItemQty.getValue();
                double itemPrice = (double) myItemPrice.getValue();
                String itemCnd = (String) myItemCnd.getSelectedItem();
                String itemSize = (String) myItemSize.getSelectedItem();
                String itemComment = myItemComments.getText();

                Item item = new Item(0, user.getUserID(), itemName, itemDesc,
                        itemQty, itemPrice, itemCnd, itemSize, itemComment);
                db.start();
                boolean noProblem = db.addItem(item);
                db.close();
                if (noProblem)
                JOptionPane.showMessageDialog(myMainScreen,
                        "Your item has been successfully entered into our system.\nYou may continue entering items or click View Storefront to review your item list.",
                        "Success!",JOptionPane.PLAIN_MESSAGE);
                else {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "There seems to have been a problem.",
                            "Failure!",JOptionPane.PLAIN_MESSAGE);
                }
            }


        }

    }




}
