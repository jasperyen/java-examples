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
public class MeasurementSet extends ComponentSet {

    public MeasurementSet() {
        setComponentName(Measurement.ComponentName);
        setTableName(Measurement.TableName);
        setAllKeyList(Measurement.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(0, "Project Name");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(1, "Type");
        map.put(2, "Dimension");
        map.put(3, "Weight");
        setGeneraColumn(map);
                
        map = new TreeMap();
        setForeignColumn(map);
    }
    
    @Override
    public Measurement newComponent(){
        return new Measurement();
    }
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        return tableMap;
    }
    
    public static class Measurement extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Project = "";
        private String Type = "";
        private String Dimension = "";
        private String Weight = "";
        
        static {
            ComponentName = "Measurement";
            MainKey = db.measurement_Table.Project;
            TableName = db.measurement_Table.TABLE_NAME;
            ProjectTableName = db2.project_measurement_Table.TABLE_NAME;

            List<String> list = new ArrayList();
            list.add(db.measurement_Table.Project);
            list.add(db.measurement_Table.Type);
            list.add(db.measurement_Table.Dimension);
            list.add(db.measurement_Table.Weight);
            AllKeyList = list;

            list = new ArrayList();
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.measurement_Table.Type);
            list.add(db.measurement_Table.Dimension);
            list.add(db.measurement_Table.Weight);
            GeneralKeyList = list;
        }

        public Measurement() {
            setComponentName(ComponentName);
            setTableName(TableName);
            setAllKeyList(AllKeyList);
            
            initializeComList();
        }

        public String getProject() {
            return Project;
        }

        public void setProject(String Project) {
            this.Project = Project;
            comList.set(0, Project);
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
            comList.set(1, Type);
        }

        public String getDimension() {
            return Dimension;
        }

        public void setDimension(String Dimension) {
            this.Dimension = Dimension;
            comList.set(2, Dimension);
        }

        public String getWeight() {
            return Weight;
        }

        public void setWeight(String Weight) {
            this.Weight = Weight;
            comList.set(3, Weight);
        }
        
        @Override
        public void setComponentByColumn (int col, String data) {
            switch (col){
                    case 0 :
                        setProject(data);
                        break;
                    case 1 :
                        setType(data);
                        break;
                    case 2 :
                        setDimension(data);
                        break;
                    case 3 :
                        setWeight(data);
                        break;
                }
        }
        
        @Override
        public List  getWrongComponentList(Map<String, List<String>> checkMap) {
            List wrongList = new ArrayList<String>();
            
            if (Project.equals(""))
                wrongList.add(MainKey);
            
            return wrongList;
        }
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            return "                <tr><td>"+ Project +"</td><td>"+
                        Type +"</td><td>"+ Dimension +"</td><td>"+ Weight +"</td></tr>\n";
        }
        
    }
}
