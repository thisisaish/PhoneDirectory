// package files;
import java.io.*;
import java.util.*;

class PhoneDir{
    public static void main(String[] args){
        File file = new File("D:/phoneBook.txt");
        Scanner sc = new Scanner(System.in);
        int choice,index;
        while(true){            
            choice = sc.nextInt();
            PhoneDir pDir = new PhoneDir();
            switch(choice){
                case 1:
                    int size = sc.nextInt();
                    String[] contacts = new String[size];
                    sc.nextLine();
                    for(index = 0;index < size;index++)
                        contacts[index] = sc.nextLine();
                    pDir.createEntries(file, contacts);
                    break;
                case 2:
                    sc.nextLine();
                    String contactName = sc.nextLine();
                    System.out.println(pDir.searchContact(file,contactName));
                    break;
                case 3:
                    pDir.displayContacts(file);
                    break;
                case 4:
                    sc.nextLine();
                    String name = sc.nextLine();
                    if(pDir.removeContacts(file,name))
                        System.out.println("Contact deleted!");
                    else
                        System.out.println("Contact does not exist");
                    break;
                case 5:
                    System.out.println("Restoring contacts.....");
                    pDir.restore(file);
                    break;
                default:
                    // sc.close();
                    System.out.println("Have a nice day!!!");
                    System.exit(0);
            }
        }
    }

    private void restore(File file){
        try{
            PrintWriter pw = new PrintWriter(file);
            File bfile = new File("D:/bfolder/phonebook.bak");
            BufferedReader br = new BufferedReader(new FileReader(bfile));
            if(!bfile.exists()){
                System.out.println("Restoration failed!");
            }else{
                String contacts;
                while((contacts = br.readLine()) != null)
                    pw.write(contacts+"\n");
                pw.flush();

                System.out.println("Contacts restored successfully!");
                pw.close();
                br.close();
            }
        }catch(IOException ex){ ex.printStackTrace(); }
    }

    private boolean removeContacts(File file,String name){
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter("D:/newFile.txt");
            String line;
            int index,count = 0;
            while((line = br.readLine()) != null){
                index = line.indexOf('-');
                if(!name.equals(line.substring(0,index))){
                    pw.write(line+"\n");
                }
                else
                    count++;
            }
            pw.flush();

            pw.close();
            br.close();
            if(count == 0)
                return false;  
            br = new BufferedReader(new FileReader("D:/newFile.txt"));
            pw = new PrintWriter(file);
            while((line = br.readLine()) != null)
                pw.write(line+"\n");
            pw.flush();

            pw.close();
            br.close();
            File fp = new File("D:/newFile.txt");
            fp.delete();

        }catch(IOException fnf){
            fnf.printStackTrace();
        }
        return true;
    }

    private boolean removeDupContacts(File file){
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> numbers = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter("D:/newFile.txt");
            String line;
            int index,dupCount = 0;
            while((line = br.readLine()) != null){
                index = line.indexOf('-');
                if(!names.contains(line.substring(0,index)) && !numbers.contains(line.substring(index+1))){
                    names.add(line.substring(0,index));
                    numbers.add(line.substring(index+1));
                    pw.write(line+"\n");
                }
                else
                    dupCount++;
            }
            names.clear();
            numbers.clear();
            pw.flush();

            pw.close();
            br.close();
            br = new BufferedReader(new FileReader("D:/newFile.txt"));
            pw = new PrintWriter(file);
            while((line = br.readLine()) != null)
                pw.write(line+"\n");
            pw.flush();

            pw.close();
            br.close();
            File fp = new File("D:/newFile.txt");
            fp.delete();

            if(dupCount > 0)
                return true; 
                
        }catch(IOException fnf){
            fnf.printStackTrace();
        }
        return false;
    }

    private void createEntries(File file,String[] entries){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
            for(String entry : entries){
                bw.write(entry+"\n");
            }
            bw.close();
            System.out.println(entries.length + " contacts saved successfully");
            PhoneDir pd = new PhoneDir();
            pd.backupContacts(file);
        }catch(FileNotFoundException fnf){
            fnf.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    private String searchContact(File file,String name){
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String contacts,cname;
            int index;
            while((contacts = br.readLine()) != null){
                index = contacts.indexOf('-');
                cname = contacts.substring(0,index);
                System.out.println(cname);
                if(name.equals(cname)){
                    br.close();
                    return contacts+"\nContact found";
                }
            }
            br.close();
        }catch(FileNotFoundException fnf){
            fnf.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }catch(StringIndexOutOfBoundsException ioe){
            ioe.printStackTrace();
        }
        return name+"'s Contact could not be found";
    }

    private void displayContacts(File file){
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String contacts;
            while((contacts = br.readLine()) != null){
                System.out.println(contacts);
            }
            br.close();
        }catch(FileNotFoundException fnf){
            fnf.printStackTrace();
        }catch(Exception ioe){
            ioe.printStackTrace();
        }
    }

    private void backupContacts(File file){
        PhoneDir pDir = new PhoneDir();
        pDir.removeDupContacts(file);
        try{
            File f = new File("D:/bfolder/phonebook.bak");
            if(!f.exists())
                f.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(f));
            ArrayList<String> contacts = new ArrayList<String>();
            String details;
            while((details = br.readLine()) != null)
                contacts.add(details);
            br.close();

            br = new BufferedReader(new FileReader(file));
            FileWriter fp = new FileWriter("D:/bfolder/phonebook.bak",true);
            PrintWriter pw = new PrintWriter(fp);
            while((details = br.readLine()) != null){
                if(!contacts.contains(details))
                    pw.write(details+"\n");
            }
            pw.flush();

            contacts.clear();
            br.close();
            pw.close();
        }catch(IOException ex){ ex.printStackTrace(); }
    }
}