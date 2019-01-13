package Crud_Software;

public class Product {
    
    private final int id;
    private final String product_id;
    private final String name;
    private final String category;
    private final String model;
    private final float price;
    private final String description;
    private final String addDate;
    private final byte[] picture;
    
    public Product(int id, String product_id, String pname, String pcategory, String pmodel, float pprice, String pdescription, 
            String pAddDate, byte[] pimg){
        this.id=id;
        this.product_id = product_id;
        this.name=pname;
        this.category = pcategory;
        this.model = pmodel;
        this.price=pprice;
        this.description = pdescription;
        this.addDate=pAddDate;
        this.picture = pimg;
    }
    
    public int getId(){
        return id;
    }
    
    public String getProductId(){
        return product_id;
    }
    
    public String getName(){
        return name;
    }
    
    public String getCategory(){
        return category;
    }
    
    public String getModel(){
        return model;
    }
    
    public float getPrice(){
        return price;
    }
    
    public String getDescription(){
        return description;
    }
    
    public String getAddDate(){
        return addDate;
    }
    
    public byte[] getImage(){
        return picture;
    }
    
}
