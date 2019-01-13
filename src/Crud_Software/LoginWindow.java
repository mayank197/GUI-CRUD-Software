package Crud_Software;

import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;


public class LoginWindow extends javax.swing.JFrame {

    Connection con = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    static int registered_users = 0;
    static int code, accepted_code;
    static String mail_mssg = null;
    
    public LoginWindow() {
        initComponents();
        setResizable(false);
        signup_button.setEnabled(false);
    }
    
    
    /* Storing Encrypted Password
    
    public byte[] getEncryptedPassword(String password, byte[] salt) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 20000;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
        return f.generateSecret(spec).getEncoded();
    }
    
    public boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt) {
        byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }
    
    public byte[] generateSalt() throws NoSuchAlgorithmException {
        // Don't use random, instead use SecureRandom
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return salt;
    }

    */
    
    public void DisappearStatus(){
        signup_username_status.setVisible(false);
        signup_password_status.setVisible(false);
        signup_email_status.setVisible(false);
    }
    
    public void DisappearSignupFields(){
        signup_username.setText(null);
        signup_password.setText(null);
        signup_email.setText(null);
    }
    
    public void DisappearSignup(){
        DisappearStatus();
        DisappearSignupFields();
    }
    
    public boolean EnableSignUp(){
        if(signup_username_status.getText().endsWith("available") && 
                signup_password_status.getText().isEmpty() &&
                !signup_email_status.getText().equals("Email ID already exists") &&
                !signup_email_status.getText().equals("Email ID is invalid") && 
                signup_email_status.getText().equals("Email ID is valid and available")){
            //signup_button.setEnabled(true);
            return true;
        }
        else{
            //signup_button.setEnabled(false);
            return false;
        }
    }
    
    public void Signup_Ready(){
        if(EnableSignUp())
            signup_button.setEnabled(true);
        else
            signup_button.setEnabled(false);
    }
    
    public Connection getConnection(){
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login-system?autoReconnect=true&useSSL=false","root","redhat");
//            JOptionPane.showMessageDialog(null, "Connected");             // Also for testing
            return con;
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }        
    }
    
    
    public static boolean sendMail(String from, String password, String message, String to[]){
        
        String host = "smtp.gmail.com";
        Properties props = System.getProperties();
        Set set=props.entrySet();  
  
        Iterator itr=set.iterator();  
        while(itr.hasNext()){  
            Map.Entry entry=(Map.Entry)itr.next();  
            System.out.println(entry.getKey()+" = "+entry.getValue());  
        }  
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "true");
        
        Session session = Session.getDefaultInstance(props);
        MimeMessage mimeMessage = new MimeMessage(session);
        
        try{
            mimeMessage.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];
            for(int i=0;i<to.length;i++){
                toAddress[i] = new InternetAddress(to[i]);
            }
            
            // Add all addresses to mimeMessage
            for(int i=0;i<toAddress.length;i++){
                mimeMessage.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }
            
            // add subject
            mimeMessage.setSubject("Confirmation Mail from CRUD Software");           
            
            Random r = new Random();
            code = r.nextInt();
            // set message to MimeMessage
            mail_mssg = "\nConfirmation Code : "+code;
            mimeMessage.setText(mail_mssg);
            
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            
            transport.close();
            return true;                
        }
        
        catch(MessagingException ex){
           
            ex.printStackTrace();
        }
        return false;   
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        SubHeading_Label = new javax.swing.JLabel();
        Heading_Label = new javax.swing.JLabel();
        login_panel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        login_button = new javax.swing.JButton();
        login_reset = new javax.swing.JButton();
        signup_panel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        signup_username = new javax.swing.JTextField();
        signup_password = new javax.swing.JPasswordField();
        signup_button = new javax.swing.JButton();
        signup_reset = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        signup_email = new javax.swing.JTextField();
        signup_email_status = new javax.swing.JLabel();
        signup_username_status = new javax.swing.JLabel();
        signup_password_status = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login/Signup Window");
        setBackground(new java.awt.Color(0, 0, 0));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 255, 243));
        jPanel1.setOpaque(false);

        SubHeading_Label.setBackground(new java.awt.Color(0, 102, 153));
        SubHeading_Label.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        SubHeading_Label.setForeground(new java.awt.Color(255, 204, 0));
        SubHeading_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SubHeading_Label.setText("This is a Certified Developer's Access to the CRUD DATABASE WINDOW. Login with your credentials");
        SubHeading_Label.setOpaque(true);

        Heading_Label.setBackground(new java.awt.Color(0, 51, 204));
        Heading_Label.setFont(new java.awt.Font("Comic Sans MS", 1, 32)); // NOI18N
        Heading_Label.setForeground(new java.awt.Color(255, 255, 0));
        Heading_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Heading_Label.setText("Access to CRUD Window for Developers");
        Heading_Label.setOpaque(true);

        login_panel.setBackground(new java.awt.Color(66, 139, 202));
        login_panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.white), "Login", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(153, 255, 255))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("UserName : ");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Password : ");

        username.setBackground(new java.awt.Color(221, 255, 255));
        username.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        username.setForeground(new java.awt.Color(0, 0, 153));
        username.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        username.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(102, 0, 153), new java.awt.Color(51, 153, 204)));
        username.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        username.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                login_focus(evt);
            }
        });

        password.setBackground(new java.awt.Color(221, 255, 255));
        password.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        password.setForeground(new java.awt.Color(0, 0, 153));
        password.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        password.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(102, 0, 153), new java.awt.Color(51, 153, 204)));
        password.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
        });
        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordKeyPressed(evt);
            }
        });

        login_button.setBackground(new java.awt.Color(0, 51, 204));
        login_button.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        login_button.setForeground(new java.awt.Color(255, 255, 255));
        login_button.setText("LogIn");
        login_button.setToolTipText("Login to Developer Portal");
        login_button.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 255), new java.awt.Color(51, 51, 255)));
        login_button.setBorderPainted(false);
        login_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        login_button.setFocusPainted(false);
        login_button.setFocusable(false);
        login_button.setRequestFocusEnabled(false);
        login_button.setRolloverEnabled(false);
        login_button.setVerifyInputWhenFocusTarget(false);
        login_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login_buttonActionPerformed(evt);
            }
        });

        login_reset.setBackground(new java.awt.Color(0, 51, 204));
        login_reset.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        login_reset.setForeground(new java.awt.Color(255, 255, 255));
        login_reset.setText("Reset");
        login_reset.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 255), new java.awt.Color(51, 51, 255)));
        login_reset.setBorderPainted(false);
        login_reset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        login_reset.setFocusPainted(false);
        login_reset.setFocusable(false);
        login_reset.setRequestFocusEnabled(false);
        login_reset.setRolloverEnabled(false);
        login_reset.setVerifyInputWhenFocusTarget(false);
        login_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login_resetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout login_panelLayout = new javax.swing.GroupLayout(login_panel);
        login_panel.setLayout(login_panelLayout);
        login_panelLayout.setHorizontalGroup(
            login_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(login_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(login_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(login_panelLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(login_button, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(login_reset, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(64, Short.MAX_VALUE))
                    .addGroup(login_panelLayout.createSequentialGroup()
                        .addGroup(login_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(login_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(username)
                            .addComponent(password))
                        .addContainerGap())))
        );
        login_panelLayout.setVerticalGroup(
            login_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(login_panelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(login_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(login_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(login_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(login_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_reset, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(198, Short.MAX_VALUE))
        );

        signup_panel.setBackground(new java.awt.Color(66, 139, 202));
        signup_panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), null), "SignUp", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(102, 255, 255))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("UserName : ");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Password : ");

        signup_username.setBackground(new java.awt.Color(221, 255, 255));
        signup_username.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        signup_username.setForeground(new java.awt.Color(0, 0, 153));
        signup_username.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        signup_username.setToolTipText("");
        signup_username.setAutoscrolls(false);
        signup_username.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(102, 0, 153), new java.awt.Color(51, 153, 255)));
        signup_username.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        signup_username.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                signup_usernameFocusGained(evt);
            }
        });
        signup_username.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signup_usernameMouseClicked(evt);
            }
        });
        signup_username.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                signup_usernameKeyReleased(evt);
            }
        });

        signup_password.setBackground(new java.awt.Color(221, 255, 255));
        signup_password.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        signup_password.setForeground(new java.awt.Color(0, 0, 153));
        signup_password.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        signup_password.setToolTipText("");
        signup_password.setAutoscrolls(false);
        signup_password.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(102, 0, 153), new java.awt.Color(51, 153, 255)));
        signup_password.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        signup_password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                signup_passwordFocusGained(evt);
            }
        });
        signup_password.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signup_passwordMouseClicked(evt);
            }
        });
        signup_password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                signup_passwordKeyReleased(evt);
            }
        });

        signup_button.setBackground(new java.awt.Color(0, 51, 204));
        signup_button.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        signup_button.setForeground(new java.awt.Color(255, 255, 255));
        signup_button.setText("SignUp");
        signup_button.setToolTipText("Sign Up on Developer Portal");
        signup_button.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 255), new java.awt.Color(51, 51, 255)));
        signup_button.setBorderPainted(false);
        signup_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        signup_button.setFocusPainted(false);
        signup_button.setFocusable(false);
        signup_button.setRequestFocusEnabled(false);
        signup_button.setRolloverEnabled(false);
        signup_button.setVerifyInputWhenFocusTarget(false);
        signup_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signup_buttonActionPerformed(evt);
            }
        });

        signup_reset.setBackground(new java.awt.Color(0, 51, 204));
        signup_reset.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        signup_reset.setForeground(new java.awt.Color(255, 255, 255));
        signup_reset.setText("Reset");
        signup_reset.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 255), new java.awt.Color(51, 51, 255)));
        signup_reset.setBorderPainted(false);
        signup_reset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        signup_reset.setFocusPainted(false);
        signup_reset.setFocusable(false);
        signup_reset.setRequestFocusEnabled(false);
        signup_reset.setRolloverEnabled(false);
        signup_reset.setVerifyInputWhenFocusTarget(false);
        signup_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signup_resetActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Email : ");

        signup_email.setBackground(new java.awt.Color(221, 255, 255));
        signup_email.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        signup_email.setForeground(new java.awt.Color(0, 0, 153));
        signup_email.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        signup_email.setToolTipText("");
        signup_email.setAutoscrolls(false);
        signup_email.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(102, 0, 153), new java.awt.Color(51, 153, 255)));
        signup_email.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        signup_email.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                signup_emailFocusGained(evt);
            }
        });
        signup_email.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signup_emailMouseClicked(evt);
            }
        });
        signup_email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                signup_emailKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                signup_emailKeyReleased(evt);
            }
        });

        signup_email_status.setBackground(new java.awt.Color(255, 204, 102));
        signup_email_status.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        signup_email_status.setForeground(new java.awt.Color(0, 255, 51));
        signup_email_status.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        signup_username_status.setBackground(new java.awt.Color(255, 204, 102));
        signup_username_status.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        signup_username_status.setForeground(new java.awt.Color(0, 255, 51));
        signup_username_status.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        signup_password_status.setBackground(new java.awt.Color(255, 204, 102));
        signup_password_status.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        signup_password_status.setForeground(new java.awt.Color(0, 255, 51));
        signup_password_status.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout signup_panelLayout = new javax.swing.GroupLayout(signup_panel);
        signup_panel.setLayout(signup_panelLayout);
        signup_panelLayout.setHorizontalGroup(
            signup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signup_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(signup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signup_panelLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(signup_username, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signup_panelLayout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addGroup(signup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signup_panelLayout.createSequentialGroup()
                                .addGroup(signup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(signup_username_status, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                    .addComponent(signup_password_status, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(signup_email_status, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
                            .addComponent(signup_email, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(signup_panelLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(signup_password, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signup_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(signup_button, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(signup_reset, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93))
        );
        signup_panelLayout.setVerticalGroup(
            signup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signup_panelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(signup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(signup_username, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(signup_username_status, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(signup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(signup_password, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(signup_password_status, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(signup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(signup_email, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(signup_email_status, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(signup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(signup_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(signup_reset, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(login_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(signup_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(Heading_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(SubHeading_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Heading_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SubHeading_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(signup_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(login_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void login_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_buttonActionPerformed
        String sql = "select * from userinfo where username=? and password=? ";
        try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login-system","root","redhat");
                pst = con.prepareStatement(sql);
                pst.setString(1, username.getText());
                pst.setString(2, password.getText());
                rs = pst.executeQuery();
                
                if(rs.next()){
                    JOptionPane.showMessageDialog(null, "Login Successful");
                    MainFrame mainframe = new MainFrame();
                    mainframe.setVisible(true);       
                }
                else if(username.getText()==null || password.getText()==null){
                    JOptionPane.showMessageDialog(null, "One or More Fields are empty");
                }
                else{
                    JOptionPane.showMessageDialog(null, "Wrong Username or Password");
                }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_login_buttonActionPerformed

    private void login_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_resetActionPerformed
        username.setText(null);
        password.setText(null);
    }//GEN-LAST:event_login_resetActionPerformed

    private void signup_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signup_buttonActionPerformed
        if(!EnableSignUp()){
            JOptionPane.showMessageDialog(null, "Some error occured");
        }
        else if(EnableSignUp()==true){
            String[] to = {signup_email.getText()};
                if(sendMail("mayanksh1976@gmail.com", "MayankCool@19796", mail_mssg ,to)){
                    JOptionPane.showMessageDialog(null, "Message Sent Successfully. Check your mail and enter the verification code");
                    String answer = JOptionPane.showInputDialog("Enter Code : ");
                    accepted_code = Integer.parseInt(answer);
                    if(accepted_code == code){
                        JOptionPane.showMessageDialog(null, "Email verified");
                        String sql = "insert into userinfo(username,password,email) values(?,?,?)";
                        try{
                            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login-system","root","redhat");
                            pst = con.prepareStatement(sql);
                            pst.setString(1, signup_username.getText());
                            pst.setString(2, signup_password.getText());
                            pst.setString(3, signup_email.getText());

                            String emailVerifier = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                            Pattern patt = Pattern.compile(emailVerifier);
                            if(patt.matcher(signup_email.getText()).matches()==true){

                                pst.execute();
                                JOptionPane.showMessageDialog(null, "User Registered");
                                DisappearSignup();
                                ++registered_users;
                                System.out.println("\n\nRegistered Users so far : "+registered_users);
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Email is not valid");
                        }
                        catch(Exception e){
                            JOptionPane.showMessageDialog(null, "Could not Insert. Some error occured");
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Code not correct.");
                }
        }
        else{
            JOptionPane.showMessageDialog(null, "Some error occured. Try Again");
            System.out.print("Message not sent. Some problem occured");
        }        
    }//GEN-LAST:event_signup_buttonActionPerformed

    private void signup_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signup_resetActionPerformed
        DisappearSignup();
    }//GEN-LAST:event_signup_resetActionPerformed

    private void signup_usernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_signup_usernameFocusGained
        username.setText(null);
        password.setText(null);
    }//GEN-LAST:event_signup_usernameFocusGained

    private void signup_passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_signup_passwordFocusGained
        if(signup_username.getText().isEmpty()){
            signup_username_status.setVisible(true);
            signup_username_status.setText("Username can't be empty");
        }
        username.setText(null);
        password.setText(null);
    }//GEN-LAST:event_signup_passwordFocusGained

    private void login_focus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_login_focus
       DisappearSignup();
    }//GEN-LAST:event_login_focus

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        DisappearSignup();
    }//GEN-LAST:event_passwordFocusGained

    private void signup_emailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_signup_emailFocusGained
        if(signup_email.getText().isEmpty()){
            signup_email_status.setVisible(true);
            signup_email_status.setText("Email is required");
        }
        if(signup_username.getText().isEmpty() && signup_password.getText().isEmpty()){
            signup_username_status.setVisible(true);
            signup_username_status.setText("Username can't be empty");
            signup_password_status.setVisible(true);
            signup_password_status.setText("Password can't be empty");
        }
        else if(signup_username.getText().isEmpty() && !signup_password.getText().isEmpty())
            signup_username_status.setText("Username can't be empty");
        else if(!signup_username.getText().isEmpty() && signup_password.getText().isEmpty())
            signup_password_status.setText("Password can't be empty");
        Signup_Ready();
    }//GEN-LAST:event_signup_emailFocusGained

    private void signup_usernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_signup_usernameKeyReleased
        if(signup_username.getText().isEmpty()){
            signup_username_status.setVisible(true);
            signup_username_status.setText("Username can't be empty");
        }
        else if(!signup_username.getText().isEmpty()){
            int len = signup_username.getText().length();
            if(len<=5){
                System.out.print(len);
                signup_username_status.setVisible(true);
                signup_username_status.setText("Username can't be less than 6 characters");
            }
            else{
                try{
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login-system","root","redhat");
                    Statement stmt = con.createStatement();
                    String sql = "select * from userinfo where username = '"+signup_username.getText() + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    if(rs.next()){
                        signup_username_status.setText("Username '" +signup_username.getText() +"' already exists");
                    }
                    else{
                        signup_username_status.setText("Username '" +signup_username.getText() +"' is available");
                    }
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }
        Signup_Ready();
    }//GEN-LAST:event_signup_usernameKeyReleased

    private void signup_usernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signup_usernameMouseClicked
        if(signup_username.getText().isEmpty()){
            signup_username_status.setVisible(true);
            signup_username_status.setText("Username can't be empty");
        }
        Signup_Ready();
    }//GEN-LAST:event_signup_usernameMouseClicked

    private void signup_passwordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signup_passwordMouseClicked
        if(signup_password.getText().isEmpty()){
            signup_password_status.setVisible(true);
            signup_password_status.setText("Password can't be empty");
        }
        Signup_Ready();
    }//GEN-LAST:event_signup_passwordMouseClicked

    private void signup_passwordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_signup_passwordKeyReleased
        if(signup_password.getText().isEmpty()){
            signup_password_status.setVisible(true);
            signup_password_status.setText("Password can't be empty");
        }
        else if(!signup_password.getText().isEmpty()){
            int len = signup_password.getText().length();
            if(len<=7){
                signup_password_status.setVisible(true);
                signup_password_status.setText("Password can't be less than 8 characters");
            }
            else{
                signup_password_status.setText("");
                signup_password_status.setVisible(false);
            }
        }
        Signup_Ready();
    }//GEN-LAST:event_signup_passwordKeyReleased

    private void signup_emailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_signup_emailKeyReleased
        if(signup_email.getText().isEmpty()){
            signup_email_status.setVisible(true);
            signup_email_status.setText("Email is required");
        }
        else{
            try{
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login-system","root","redhat");
                Statement stmt = con.createStatement();
                String sql = "select * from userinfo where email = '"+signup_email.getText() + "'";
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next())
                    signup_email_status.setText("Email ID already exists");
                else{
                    String emailVerifier = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    Pattern patt = Pattern.compile(emailVerifier);
                    if(patt.matcher(signup_email.getText()).matches())
                        signup_email_status.setText("Email ID is valid and available");
                    else
                        signup_email_status.setText("Email ID is invalid");
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        Signup_Ready();
    }//GEN-LAST:event_signup_emailKeyReleased

    private void signup_emailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signup_emailMouseClicked
        if(signup_email.getText().isEmpty()){
            signup_email_status.setVisible(true);
            signup_email_status.setText("Email ID is required");
        }
        Signup_Ready();
    }//GEN-LAST:event_signup_emailMouseClicked

    private void passwordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyPressed
        if(!username.getText().isEmpty()){
            if(evt.getKeyCode() == KeyEvent.VK_ENTER){
                password.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        login_buttonActionPerformed(e);
                    }
                });
            }
        }
    }//GEN-LAST:event_passwordKeyPressed

    private void signup_emailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_signup_emailKeyPressed
        if(EnableSignUp()){
            if(evt.getKeyCode() == KeyEvent.VK_ENTER){
                signup_email.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        signup_buttonActionPerformed(e);
                    }
                });
            }
        }
    }//GEN-LAST:event_signup_emailKeyPressed
    
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Heading_Label;
    private javax.swing.JLabel SubHeading_Label;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton login_button;
    private javax.swing.JPanel login_panel;
    private javax.swing.JButton login_reset;
    private javax.swing.JPasswordField password;
    private javax.swing.JButton signup_button;
    private javax.swing.JTextField signup_email;
    private javax.swing.JLabel signup_email_status;
    private javax.swing.JPanel signup_panel;
    private javax.swing.JPasswordField signup_password;
    private javax.swing.JLabel signup_password_status;
    private javax.swing.JButton signup_reset;
    private javax.swing.JTextField signup_username;
    private javax.swing.JLabel signup_username_status;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}