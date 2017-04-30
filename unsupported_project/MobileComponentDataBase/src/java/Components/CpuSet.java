/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataBaseInfo.DataBaseName.db;
import DataBaseInfo.DataBaseName.db2;
import Servelt.MySQLSyntax;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author Jasper-Yen
 */
public class CpuSet extends ComponentSet {
    
    
    public CpuSet() {
        setComponentName(CPU.ComponentName);
        setTableName(CPU.TableName);
        setAllKeyList(CPU.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(2, "Model Name");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(3, "Max Freq. (GHz)");
        map.put(4, "Core/Threats");
        map.put(5, "Gfx");
        map.put(6, "TDP");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        map.put(1, "Code Name");
        setForeignColumn(map);
    }
    
    @Override
    public CPU newComponent(){
        return new CPU();
    }

    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        tableMap.put(db.cpu_code_name_Table.TABLE_NAME, db.cpu_code_name_Table.CodeName);
        return tableMap;
    }
    
    
    public static class CPU extends Component{
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Vendor = "";
        private String CodeName = "";
        private String ModelName = "";
        private String MaxCoreFreq = "";
        private String CoreThreats = "";
        private String Gfx = "";
        private String TDP = "";
        
        static {
            ComponentName = "CPU";
            MainKey = db.cpu_Table.ModelName;
            TableName = db.cpu_Table.TABLE_NAME;
            ProjectTableName = db2.project_cpu_Table.TABLE_NAME;

            List<String> list = new ArrayList();
            list.add(db.cpu_Table.Vendor);
            list.add(db.cpu_Table.CodeName);
            list.add(db.cpu_Table.ModelName);
            list.add(db.cpu_Table.MaxCoreFreq);
            list.add(db.cpu_Table.CoreThreats);
            list.add(db.cpu_Table.Gfx);
            list.add(db.cpu_Table.TDP);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.cpu_Table.Vendor);
            list.add(db.cpu_Table.CodeName);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.cpu_Table.MaxCoreFreq);
            list.add(db.cpu_Table.CoreThreats);
            list.add(db.cpu_Table.Gfx);
            list.add(db.cpu_Table.TDP);
            GeneralKeyList = list;
        }
        
        public CPU(){
            setComponentName(ComponentName);
            setTableName(TableName);
            setAllKeyList(AllKeyList);
            
            initializeComList();
        }

        public String getVendor() {
            return Vendor;
        }

        public void setVendor(String Vendor) {
            this.Vendor = Vendor;
            comList.set(0, Vendor);
        }

        public String getCodeName() {
            return CodeName;
        }

        public void setCodeName(String CodeName) {
            this.CodeName = CodeName;
            comList.set(1, CodeName);
        }

        public String getModelName() {
            return ModelName;
        }

        public void setModelName(String ModelName) {
            this.ModelName = ModelName;
            comList.set(2, ModelName);
        }

        public String getMaxCoreFreq() {
            return MaxCoreFreq;
        }

        public void setMaxCoreFreq(String MaxCoreFreq) {
            this.MaxCoreFreq = MaxCoreFreq;
            comList.set(3, MaxCoreFreq);
        }

        public String getCoreThreats() {
            return CoreThreats;
        }

        public void setCoreThreats(String CoreThreats) {
            this.CoreThreats = CoreThreats;
            comList.set(4, CoreThreats);
        }

        public String getGfx() {
            return Gfx;
        }

        public void setGfx(String Gfx) {
            this.Gfx = Gfx;
            comList.set(5, Gfx);
        }

        public String getTDP() {
            return TDP;
        }

        public void setTDP(String TDP) {
            this.TDP = TDP;
            comList.set(6, TDP);
        }
    
        @Override
        public void setComponentByColumn (int col, String data) {
            switch (col){
                    case 0 :
                        setVendor(data);
                        break;
                    case 1 :
                        setCodeName(data);
                        break;
                    case 2 :
                        setModelName(data);
                        break;
                    case 3 :
                        setMaxCoreFreq(data);
                        break;
                    case 4 :
                        setCoreThreats(data);
                        break;
                    case 5 :
                        setGfx(data);
                        break;
                    case 6 :
                        setTDP(data);
                        break;
                }
        }
        

        @Override
        public List  getWrongComponentList(Map<String, List<String>> checkMap) {
            List wrongList = new ArrayList<String>();
            
            if (ModelName.equals(""))
                wrongList.add(MainKey);
            
            for (Iterator iter = checkMap.entrySet().iterator(); iter.hasNext(); ) {
                Entry<String, List<String>> entry = (Entry<String, List<String>>) iter.next();
                boolean boo = false;
                switch (entry.getKey()) {
                    case db.vendor_Table.Vendor : 
                        for (Object obj : entry.getValue()) {
                            if (Vendor.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.cpu_Table.Vendor);
                        break;
                    case db.cpu_code_name_Table.CodeName : 
                        for (Object obj : entry.getValue()) {
                            if (CodeName.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.cpu_Table.CodeName);
                        break;
                }
            }
            return wrongList;
        }
        
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            String ventd = "<td>";
            String codtd = "<td>";
            for (Object obj : wrongList) {
                switch ((String)obj) {
                    case db.cpu_Table.Vendor : {
                       ventd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.cpu_Table.CodeName : {
                        codtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + ventd+ Vendor +"</td>" + codtd + CodeName +"</td><td>"+ ModelName +"</td><td>"+
                        MaxCoreFreq +"</td><td>"+ CoreThreats +"</td><td>"+ Gfx +"</td><td>"+ TDP +"</td></tr>\n";
        }

    }
}
