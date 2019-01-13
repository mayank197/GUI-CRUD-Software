package Crud_Software;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.prefs.Preferences;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MainFrame extends javax.swing.JFrame {
    
    public MainFrame() {
        initComponents();
        Show_Products_In_JTable();
        setTableAlignment(JTable_Products);
//        getConnection();          // Just to check Connection
    }
    
    String ImgPath=null ;
    int pos = 0;
    
    public Connection getConnection(){
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/products_db","root","redhat");
//            JOptionPane.showMessageDialog(null, "Connected");             // Also for testing
            return con;
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null, "Not Connected"); 
            return null;
        }        
    } 
    
    // Change default background and foreground of jComboBox
    public void ComboBoxColorChange(){
        jcb_category.setRenderer(new DefaultListCellRenderer(){
            @Override
            public void paint(Graphics g){
                setBackground(Color.WHITE);
                setForeground(Color.RED);
                super.paint(g);
            }
        });
    }
    
    // Center alignment of Table Header and Table Values
    public void setTableAlignment(JTable jTable){
        JTableHeader jTableHeader = jTable.getTableHeader();
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) jTable.getTableHeader().getDefaultRenderer();
        jTableHeader.setDefaultRenderer(renderer);
        renderer.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        
        // Aligning Table Content to Center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        int rowNumber = jTable.getColumnCount();
        for(int i=0;i<rowNumber;i++)
            jTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);            
    }
    
    // Disabling the first value in JComboBox - Category Selection
    class MyComboModel extends DefaultComboBoxModel<String>{
        public MyComboModel(){}
        public MyComboModel(Vector<String> items){
            super(items);
        }
        @Override
        public void setSelectedItem(Object item){
            if(item.toString().startsWith("Select"))
                return;
            super.setSelectedItem(item);
        }
    }
    
    // Check Input Fields
    
    public boolean checkInputs(){
        if(txt_id.getText().isEmpty() || txt_name.getText().isEmpty() || jcb_category.getSelectedIndex()==0 || 
                txt_model.getText().isEmpty() || txt_price.getText().isEmpty() || 
                txt_AddDate.getDate()==null || txt_desc.getText().isEmpty()){
            return false;
        }
        else{
            try{
                Float.parseFloat(txt_price.getText());
                return true;
            }
            catch(Exception ex){
                return false;
            }
        }
    }
    
    // Resize Image
    
    public ImageIcon ResizeImage(String imagePath, byte[] pic){
        ImageIcon myImage = null;
        
        if(imagePath!=null){
            myImage = new ImageIcon(imagePath);
        }
        else{
            myImage = new ImageIcon(pic);
        }
        
        Image img = myImage.getImage();
        Image img2 = img.getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(img2);
        return image;
    }
    
    
    // Display Data in JTable
    
    // 1 - Fill ArrayList 
    
    public ArrayList<Product> getProductList(){
        ArrayList<Product> productList = new ArrayList<Product>();
            Connection con = getConnection();
            String query = "SELECT * FROM products";
            
            Statement st;
            ResultSet rs;
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            Product product;
            
            while(rs.next()){
                 product = new Product(rs.getInt("id"), rs.getString("product_id"), rs.getString("name"), 
                         rs.getString("category"),rs.getString("model"), Float.parseFloat(rs.getString("price")), 
                         rs.getString("description"), rs.getString("add_date"),rs.getBytes("image"));
                 productList.add(product);
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return productList;               
    }
    
    // 2 - Populate the JTable
    
    public void Show_Products_In_JTable(){
        ArrayList<Product> list = getProductList();
        DefaultTableModel model = (DefaultTableModel)JTable_Products.getModel();
        
        // Clearing JTable Content
        model.setRowCount(0);
        
        Object[] row = new Object[7];
        for(int i=0;i<list.size();i++){
            row[0] = list.get(i).getProductId();
            row[1] = list.get(i).getName();
            row[2] = list.get(i).getCategory();
            row[3] = list.get(i).getModel();
            row[4] = list.get(i).getPrice();
            row[5] = list.get(i).getDescription();
            row[6] = list.get(i).getAddDate();
            model.addRow(row);            
        }
    }
    
    public void ShowItem(int index){
        txt_id.setText(getProductList().get(index).getProductId());
        txt_id.setEditable(false);
        txt_name.setText(getProductList().get(index).getName());
        jcb_category.setSelectedItem(getProductList().get(index).getCategory().toString());
        jcb_category.setEnabled(false);
        //ComboBoxColorChange();
        txt_model.setText(getProductList().get(index).getModel());
        txt_price.setText(Float.toString(getProductList().get(index).getPrice()));
        txt_desc.setText(getProductList().get(index).getDescription());
        
        try{
            Date addDate = null;
            txt_AddDate.setEditable(false);
            addDate = new SimpleDateFormat("yyyy-MM-dd").parse((String)getProductList().get(index).getAddDate());
            txt_AddDate.setDate(addDate);
        }
        catch(ParseException ex){
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        lbl_image.setIcon(ResizeImage(null, getProductList().get(index).getImage()));
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        txt_name = new javax.swing.JTextField();
        txt_price = new javax.swing.JTextField();
        txt_AddDate = new org.jdesktop.swingx.JXDatePicker();
        lbl_image = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTable_Products = new javax.swing.JTable();
        Btn_Upload_Image = new javax.swing.JButton();
        Btn_Insert = new javax.swing.JButton();
        update_button = new javax.swing.JButton();
        delete_button = new javax.swing.JButton();
        Btn_First = new javax.swing.JButton();
        Btn_Prev = new javax.swing.JButton();
        Btn_Last = new javax.swing.JButton();
        Btn_Next = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        clear_button = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_desc = new javax.swing.JEditorPane();
        jLabel6 = new javax.swing.JLabel();
        jcb_category = new javax.swing.JComboBox<>(new MyComboModel());
        Btn_Add_Image = new javax.swing.JButton();
        Btn_Remove_Image = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txt_model = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Main CRUD Window");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 51, 204));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 400));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Product ID : ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Name : ");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Price : ");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Add Date : ");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Image : ");

        txt_id.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        txt_id.setForeground(new java.awt.Color(255, 0, 51));

        txt_name.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        txt_name.setForeground(new java.awt.Color(255, 0, 51));

        txt_price.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        txt_price.setForeground(new java.awt.Color(255, 0, 51));

        txt_AddDate.setForeground(new java.awt.Color(255, 0, 0));
        txt_AddDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_AddDate.setOpaque(true);
        txt_AddDate.setPreferredSize(new java.awt.Dimension(50, 20));

        lbl_image.setBackground(new java.awt.Color(255, 255, 255));
        lbl_image.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 153), 2));
        lbl_image.setOpaque(true);

        JTable_Products.setAutoCreateRowSorter(true);
        JTable_Products.setBackground(new java.awt.Color(229, 254, 229));
        JTable_Products.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        JTable_Products.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Category", "Model", "Price", "Description", "Add Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        JTable_Products.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JTable_Products.setRowHeight(50);
        JTable_Products.setSurrendersFocusOnKeystroke(true);
        JTable_Products.getTableHeader().setReorderingAllowed(false);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, JTable_Products, org.jdesktop.beansbinding.ELProperty.create("${selectedElement}"), JTable_Products, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        JTable_Products.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTable_ProductsMouseClicked(evt);
            }
        });
        JTable_Products.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTable_ProductsKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(JTable_Products);
        if (JTable_Products.getColumnModel().getColumnCount() > 0) {
            JTable_Products.getColumnModel().getColumn(0).setResizable(false);
            JTable_Products.getColumnModel().getColumn(0).setPreferredWidth(35);
            JTable_Products.getColumnModel().getColumn(1).setResizable(false);
            JTable_Products.getColumnModel().getColumn(1).setPreferredWidth(130);
            JTable_Products.getColumnModel().getColumn(2).setResizable(false);
            JTable_Products.getColumnModel().getColumn(2).setPreferredWidth(70);
            JTable_Products.getColumnModel().getColumn(3).setResizable(false);
            JTable_Products.getColumnModel().getColumn(3).setPreferredWidth(80);
            JTable_Products.getColumnModel().getColumn(4).setResizable(false);
            JTable_Products.getColumnModel().getColumn(4).setPreferredWidth(70);
            JTable_Products.getColumnModel().getColumn(5).setResizable(false);
            JTable_Products.getColumnModel().getColumn(5).setPreferredWidth(260);
            JTable_Products.getColumnModel().getColumn(6).setResizable(false);
            JTable_Products.getColumnModel().getColumn(6).setPreferredWidth(120);
        }

        Btn_Upload_Image.setBackground(new java.awt.Color(0, 51, 204));
        Btn_Upload_Image.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Btn_Upload_Image.setForeground(new java.awt.Color(255, 255, 255));
        Btn_Upload_Image.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAYANSHA\\Documents\\Mayank\\Projects\\NetBeansProjects\\MinorProject\\icons\\upload2.png")); // NOI18N
        Btn_Upload_Image.setToolTipText("Upload Image");
        Btn_Upload_Image.setBorderPainted(false);
        Btn_Upload_Image.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn_Upload_Image.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Upload_ImageActionPerformed(evt);
            }
        });

        Btn_Insert.setBackground(new java.awt.Color(41, 181, 86));
        Btn_Insert.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Btn_Insert.setForeground(new java.awt.Color(255, 255, 255));
        Btn_Insert.setText("Insert");
        Btn_Insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_InsertActionPerformed(evt);
            }
        });

        update_button.setBackground(new java.awt.Color(41, 181, 46));
        update_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        update_button.setForeground(new java.awt.Color(255, 255, 255));
        update_button.setText("Update");
        update_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_buttonActionPerformed(evt);
            }
        });

        delete_button.setBackground(new java.awt.Color(41, 181, 86));
        delete_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        delete_button.setForeground(new java.awt.Color(255, 255, 255));
        delete_button.setText("Delete");
        delete_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_buttonActionPerformed(evt);
            }
        });

        Btn_First.setBackground(new java.awt.Color(217, 83, 79));
        Btn_First.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Btn_First.setForeground(new java.awt.Color(255, 255, 255));
        Btn_First.setText("First");
        Btn_First.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Btn_FirstMouseClicked(evt);
            }
        });

        Btn_Prev.setBackground(new java.awt.Color(217, 83, 79));
        Btn_Prev.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Btn_Prev.setForeground(new java.awt.Color(255, 255, 255));
        Btn_Prev.setText("Previous");
        Btn_Prev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Btn_PrevMouseClicked(evt);
            }
        });

        Btn_Last.setBackground(new java.awt.Color(217, 83, 79));
        Btn_Last.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Btn_Last.setForeground(new java.awt.Color(255, 255, 255));
        Btn_Last.setText("Last");
        Btn_Last.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Btn_LastMouseClicked(evt);
            }
        });

        Btn_Next.setBackground(new java.awt.Color(217, 83, 79));
        Btn_Next.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Btn_Next.setForeground(new java.awt.Color(255, 255, 255));
        Btn_Next.setText("Next");
        Btn_Next.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Btn_NextMouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Description :");

        clear_button.setBackground(new java.awt.Color(41, 181, 86));
        clear_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        clear_button.setForeground(new java.awt.Color(255, 255, 255));
        clear_button.setText("Clear");
        clear_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_buttonActionPerformed(evt);
            }
        });

        txt_desc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jScrollPane2.setViewportView(txt_desc);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Category : ");

        jcb_category.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jcb_category.setForeground(new java.awt.Color(153, 0, 153));
        jcb_category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Category", "Mobiles", "Fashion", "Electronics", "Beauty & Health" }));
        jcb_category.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        Btn_Add_Image.setBackground(new java.awt.Color(0, 51, 204));
        Btn_Add_Image.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Btn_Add_Image.setForeground(new java.awt.Color(0, 51, 204));
        Btn_Add_Image.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAYANSHA\\Documents\\Mayank\\Projects\\NetBeansProjects\\MinorProject\\icons\\add7.png")); // NOI18N
        Btn_Add_Image.setToolTipText("Add more images");
        Btn_Add_Image.setBorderPainted(false);
        Btn_Add_Image.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn_Add_Image.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Add_ImageActionPerformed(evt);
            }
        });

        Btn_Remove_Image.setBackground(new java.awt.Color(0, 51, 204));
        Btn_Remove_Image.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Btn_Remove_Image.setForeground(new java.awt.Color(255, 255, 255));
        Btn_Remove_Image.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAYANSHA\\Documents\\Mayank\\Projects\\NetBeansProjects\\MinorProject\\icons\\cancel2.png")); // NOI18N
        Btn_Remove_Image.setToolTipText("Remove Image");
        Btn_Remove_Image.setBorderPainted(false);
        Btn_Remove_Image.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn_Remove_Image.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Remove_ImageActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Model : ");

        txt_model.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        txt_model.setForeground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Btn_Insert, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(update_button, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(delete_button, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clear_button, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel5))
                                .addGap(13, 13, 13)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_price, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_AddDate, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(lbl_image, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(5, 5, 5)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(Btn_Upload_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Btn_Add_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(Btn_Remove_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_model, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcb_category, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(Btn_Prev, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(Btn_Next, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(Btn_First, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(Btn_Last, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 895, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcb_category, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_price, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_model, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_AddDate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(Btn_Upload_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(44, 44, 44)
                                        .addComponent(Btn_Add_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(Btn_Remove_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(11, 11, 11))
                                    .addComponent(lbl_image, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(95, 95, 95)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(delete_button, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Btn_Last, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Btn_Prev, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Btn_Next, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Btn_First, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Btn_Insert, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(update_button, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clear_button, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        Font f1 = jcb_category.getFont();
        Font f2 = new Font("Comic Sans",0,14);

        jcb_category.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof JComponent)
                return (JComponent) value;

                boolean itemEnabled = !value.toString().startsWith("Select");
                super.getListCellRendererComponent(list, value, index,
                    isSelected && itemEnabled, cellHasFocus);

                // Render item as disabled and with different font:
                setEnabled(itemEnabled);
                setFont(itemEnabled ? f1 : f2);

                return this;
            }

        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Btn_Upload_ImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_Upload_ImageActionPerformed
        JFileChooser file = new JFileChooser();
        Preferences pref = Preferences.userRoot();
        String file_path = pref.get("DEFAULT_PATH", "");
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        file.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + 
//                "Documents\\Mayank\\Projects\\NetBeansProjects\\MinorProject\\products"));
        file.setCurrentDirectory(new File(file_path));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.image", "jpg","png");
        file.addChoosableFileFilter(filter);
        int result = file.showSaveDialog(null);
        if(result==JFileChooser.APPROVE_OPTION){
            File selectedFile = file.getSelectedFile();
            file.setCurrentDirectory(selectedFile);
            String path = selectedFile.getAbsolutePath();
            pref.put("DEFAULT_PATH", path);
            lbl_image.setIcon(ResizeImage(path, null));
            ImgPath = path;
        }
        else{
            System.out.println("No File Selected");
        }
    }//GEN-LAST:event_Btn_Upload_ImageActionPerformed

    private void Btn_InsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_InsertActionPerformed
        if(checkInputs() && ImgPath != null){            
            try {
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO PRODUCTS (product_id,name,category,model,"
                        + "price,description,add_date,image) values(?,?,?,?,?,?,?,?) ");
                ps.setString(1, txt_id.getText());
                ps.setString(2, txt_name.getText());
                ps.setString(3, jcb_category.getSelectedItem().toString());
                ps.setString(4, txt_model.getText());
                ps.setString(5, txt_price.getText());
                ps.setString(6, txt_desc.getText());
                //Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                //String addDate = dateFormat.format(date);
                String addDate = dateFormat.format(txt_AddDate.getDate()).toString();
                ps.setString(7, addDate);
                
                InputStream img = new FileInputStream(new File(ImgPath));
                ps.setBlob(8, img);
                ps.executeUpdate();
                Show_Products_In_JTable();
//                JTable_Products.scrollRectToVisible(JTable_Products.getCellRect(JTable_Products.getRowCount()-1, JTable_Products.getColumnCount(), true));
                JOptionPane.showMessageDialog(null, "Data Inserted");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "One or more fields are empty");
        }
        
        System.out.println("Name => " +txt_name.getText() );
        System.out.println("Price => " +txt_price.getText() );
        System.out.println("Description => " +txt_desc.getText() );
        System.out.println("Image => " +ImgPath );
        
    }//GEN-LAST:event_Btn_InsertActionPerformed

    private void update_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_buttonActionPerformed
           if(checkInputs() && txt_id.getText() != null){
               String UpdateQuery = null;
               PreparedStatement ps = null;
               Connection con = getConnection();
               
               // Update without image
               
               if(ImgPath == null){
                   try {
                       UpdateQuery = "UPDATE products SET name = ?, price = ?, description = ?, add_date = ? WHERE product_id = ?";
                       ps = con.prepareStatement(UpdateQuery);
                       ps.setString(1, txt_name.getText());
                       ps.setString(2, txt_price.getText());
                       ps.setString(3, txt_desc.getText());
                       //Date date = new Date();
                       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                       //String addDate = dateFormat.format(date);
                       String addDate = dateFormat.format(txt_AddDate.getDate()).toString();
                       ps.setString(4, addDate);
                       ps.setString(5, txt_id.getText());
                       
                       ps.executeUpdate();
                       Show_Products_In_JTable();
                       JOptionPane.showMessageDialog(null, "Product Updated");
                   } 
                   catch (SQLException ex) {
                       Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                   }
               } 
               
               // Update with Image
               
               else{
                   try {
                       InputStream img = new FileInputStream(new File(ImgPath));
                       UpdateQuery = "UPDATE products SET name = ?, price = ?, description = ?, add_date = ?, image = ? WHERE product_id = ?";
                       
                       ps = con.prepareStatement(UpdateQuery);
                       ps.setString(1, txt_name.getText());
                       ps.setString(2, txt_price.getText());
                       ps.setString(3, txt_desc.getText());
                       //Date date = new Date();
                       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                       //String addDate = dateFormat.format(date);
                       String addDate = dateFormat.format(txt_AddDate.getDate()).toString();
                       ps.setString(4, addDate);
                       ps.setBlob(5, img);
                       ps.setString(6, txt_id.getText());
                       
                       ps.executeUpdate();
                       Show_Products_In_JTable();
                       JOptionPane.showMessageDialog(null, "Product Updated");
                       
                   } 
                   catch (Exception ex) {
                       JOptionPane.showMessageDialog(null, ex.getMessage());
                   }
               }
           }
           else{
               JOptionPane.showMessageDialog(null, "One or more fields are Empty or Wrong");
               
           }
    }//GEN-LAST:event_update_buttonActionPerformed

    private void delete_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_buttonActionPerformed
        if(!txt_id.getText().equals("")){
            try {
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement("DELETE FROM products WHERE product_id = ?");
                String id = txt_id.getText();
                ps.setString(1, id);
                ps.executeUpdate();
                txt_name.setText(null);
                jcb_category.setSelectedIndex(0);
                txt_model.setText(null);
                txt_price.setText(null);
                txt_desc.setText(null);
                txt_id.setText(null);
                txt_AddDate.setDate(null);
                lbl_image.setIcon(null);
                Show_Products_In_JTable();
                JOptionPane.showMessageDialog(null, "Product Deleted");
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Product Not Deleted. Try Again");
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"Product not Deleted : No ID to Delete");
        }
    }//GEN-LAST:event_delete_buttonActionPerformed

    private void JTable_ProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTable_ProductsMouseClicked
       
        int index = JTable_Products.getSelectedRow();
        ShowItem(index);
        
    }//GEN-LAST:event_JTable_ProductsMouseClicked

    private void Btn_NextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn_NextMouseClicked
        pos++;
        
        if(pos >= getProductList().size()){
            pos = getProductList().size()-1;
        }
        ShowItem(pos);   
        JTable_Products.changeSelection(pos, pos+1, false, false);
    }//GEN-LAST:event_Btn_NextMouseClicked

    private void Btn_LastMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn_LastMouseClicked
        pos = getProductList().size()-1;
        ShowItem(pos);
        JTable_Products.changeSelection(pos, getProductList().size(), false, false);
    }//GEN-LAST:event_Btn_LastMouseClicked

    private void Btn_PrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn_PrevMouseClicked
        pos--;
        
        if(pos < 0){
            pos = 0;
        }
        ShowItem(pos);
        JTable_Products.changeSelection(pos, pos-1, false, false);
    }//GEN-LAST:event_Btn_PrevMouseClicked

    private void clear_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_buttonActionPerformed
         txt_name.setText(null);
         txt_id.setEnabled(true);
         txt_id.setEditable(true);
         txt_price.setText(null);
         jcb_category.setEnabled(true);
         jcb_category.setSelectedIndex(0);
         txt_model.setText(null);
         txt_desc.setText(null);
         txt_id.setText(null);
         txt_AddDate.setDate(null);
         txt_AddDate.setEditable(true);
         lbl_image.setIcon(null);
         JTable_Products.getSelectionModel().clearSelection();
    }//GEN-LAST:event_clear_buttonActionPerformed
    
    private void JTable_ProductsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTable_ProductsKeyPressed

        int index = JTable_Products.getSelectedRow();
        ShowItem(index+1);        
        
    }//GEN-LAST:event_JTable_ProductsKeyPressed

    private void Btn_FirstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn_FirstMouseClicked
       pos=0;
       ShowItem(pos);
       JTable_Products.changeSelection(pos,0, false, false);
    }//GEN-LAST:event_Btn_FirstMouseClicked

    private void Btn_Add_ImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_Add_ImageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Btn_Add_ImageActionPerformed

    private void Btn_Remove_ImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_Remove_ImageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Btn_Remove_ImageActionPerformed

    
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_Add_Image;
    private javax.swing.JButton Btn_First;
    private javax.swing.JButton Btn_Insert;
    private javax.swing.JButton Btn_Last;
    private javax.swing.JButton Btn_Next;
    private javax.swing.JButton Btn_Prev;
    private javax.swing.JButton Btn_Remove_Image;
    private javax.swing.JButton Btn_Upload_Image;
    private javax.swing.JTable JTable_Products;
    private javax.swing.JButton clear_button;
    private javax.swing.JButton delete_button;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> jcb_category;
    private javax.swing.JLabel lbl_image;
    private org.jdesktop.swingx.JXDatePicker txt_AddDate;
    private javax.swing.JEditorPane txt_desc;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_model;
    private javax.swing.JTextField txt_name;
    private javax.swing.JTextField txt_price;
    private javax.swing.JButton update_button;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}