/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.Item;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import io.Input;



/**
 *
 * @author Jay
 * 
 * Manage all invoices generated by user purchases
 * 
 */
public class InvoiceManager {
    ArrayList<Invoice> all_invoices;
    Input in;
    Inventory inv;
    public InvoiceManager(){
        all_invoices = new ArrayList<>();
        inv = new Inventory();
        in = new Input();
        try{
            loadInvoices();
        }catch(Exception e){
            e.printStackTrace();
        }
    // call load
    }
    
    
    // load invoices
    // open the users.json file to populate the all_users arraylist
    public final void loadInvoices() throws Exception{
        Object obj = new JSONParser().parse(new FileReader("src/res/invoices.json"));
        
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;
       
        // getting list of items
        JSONArray ja = (JSONArray)jo.get("Users");
        
        // iterating Items
        Iterator itr = ja.iterator();
        
        // go through the array to get all the maps
        while(itr.hasNext()){
            Invoice temp = new Invoice();
            // reading help here
            JSONObject obj_in = (JSONObject)itr.next();
            // set up the items
            JSONArray items = (JSONArray) obj_in.get("Items");
            Iterator itr_item = items.iterator();
            while(itr_item.hasNext()){
                String id = (String)itr_item.next();
                Item hold = inv.return_by_id(id);
                temp.add_item(hold); // done adding items
            }
            // set other vars here
            temp.set_Amount((Double)obj_in.get("Amount"));
            temp.set_InvoiceCode((String)obj_in.get("Code"));
            temp.set_User((String)obj_in.get("User"));
            
            this.all_invoices.add(temp); // done
        }
    }
    
    // save invoices
    public void saveInvoices(){
       JSONObject invoices = new JSONObject();
       JSONArray list = new JSONArray();
       // go through all the users and add them to the list
       for(int i = 0; i < all_invoices.size(); i++){
           JSONObject obj = new JSONObject();
           JSONArray items = new JSONArray();
           obj.put("User", all_invoices.get(i).get_User());
           obj.put("Code", all_invoices.get(i).get_InvoiceCode());
           obj.put("Amount", all_invoices.get(i).get_Amount());
           for(Item item : all_invoices.get(i).get_Items()){
               items.add(item.item_id);
           }
           obj.put("Items", items);
           list.add(obj);
           
       }// end of for
       invoices.put("Invoices", list);
       // now write the file!
       
       try (FileWriter file = new FileWriter("src/res/invoices.json")) {

            file.write(invoices.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
   }// end
    // get/setters?
    public ArrayList<Invoice> get_invoices(){return this.all_invoices;}
    
    public void addInvoice(Invoice i){
        this.all_invoices.add(i);
        
        // then call save
        saveInvoices();
    }
    // some other system helpers
}
