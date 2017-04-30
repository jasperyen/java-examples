/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataBaseInfo.DataBaseName.db;
import DataBaseInfo.DataBaseName.db2;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Jasper-Yen
 */
public class IoPortSet extends ComponentSet {

    public IoPortSet() {
        setComponentName(IoPort.ComponentName);
        setTableName(IoPort.TableName);
        setAllKeyList(IoPort.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(0, "Standard");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(1, "Connector Type");
        map.put(2, "Description");
        setGeneraColumn(map);
                
        map = new TreeMap();
        setForeignColumn(map);
    }
    
    @Override
    public IoPort newComponent(){
        return new IoPort();
    }
    
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        return tableMap;
    }
    
    
    public static class IoPort extends Component{
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Standard = "";
        private String ConnectorType = "";
        private String Description = "";
    
        
        static {
            ComponentName = "IO port";
            MainKey = db.io_port_Table.Standard;
            TableName = db.io_port_Table.TABLE_NAME;
            ProjectTableName = db2.project_io_port_Table.TABLE_NAME;

            List<String> list = new ArrayList();
            list.add(db.io_port_Table.Standard);
            list.add(db.io_port_Table.ConnectorType);
            list.add(db.io_port_Table.Description);
            AllKeyList = list;

            list = new ArrayList();
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.io_port_Table.ConnectorType);
            list.add(db.io_port_Table.Description);
            GeneralKeyList = list;
        }

        public IoPort() {
            setComponentName(ComponentName);
            setTableName(TableName);
            setAllKeyList(AllKeyList);
            
            initializeComList();
        }

        public String getStandard() {
            return Standard;
        }

        public void setStandard(String Standard) {
            this.Standard = Standard;
            comList.set(0, Standard);
        }

        public String getConnectorType() {
            return ConnectorType;
        }

        public void setConnectorType(String ConnectorType) {
            this.ConnectorType = ConnectorType;
            comList.set(1, ConnectorType);
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
            comList.set(2, Description);
        }
        
        
        @Override
        public void setComponentByColumn (int col, String data) {
            switch (col){
                    case 0 :
                        setStandard(data);
                        break;
                    case 1 :
                        setConnectorType(data);
                        break;
                    case 2 :
                        setDescription(data);
                        break;
                }
        }
        
        
        @Override
        public List  getWrongComponentList(Map<String, List<String>> checkMap) {
            List wrongList = new ArrayList<String>();
            
            if (Standard.equals(""))
                wrongList.add(MainKey);
            
            return wrongList;
        }
        
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            return "                <tr><td>"+ Standard +"</td><td>"+
                        ConnectorType +"</td><td>"+ Description +"</td></tr>\n";
        }
        
    }
    
}
