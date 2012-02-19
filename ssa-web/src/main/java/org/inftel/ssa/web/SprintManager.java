/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.inftel.ssa.domain.Sprint;
import org.inftel.ssa.services.ResourceService;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Jesus Ruiz Oliva
 */
@ManagedBean
@SessionScoped
public class SprintManager implements Serializable{
    @EJB
    private ResourceService resources;
    private static final long serialVersionUID = 1L; 
    private LazyDataModel<Sprint> sprints;
    private Sprint currentSprint;
    
    
    public SprintManager() {
        super();
        sprints= new LazyDataModel() {            

            @Override
            public List load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder, Map filters) {
                int[] range = {first,first + pageSize};
                List<Sprint> list = null;
                // ¿Vienen ordenados?¿Como identifico los sprints?
                //list = resources.findRange(range);  
                return list;
            }
             @Override
            public void setRowIndex(int rowIndex) {
                /*
                * The following is in ancestor (LazyDataModel):
                * this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
                */
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                }
                else{
                    super.setRowIndex(rowIndex);
                }
            }            
            };
            
    }

    public LazyDataModel<Sprint> getSprint() {
        int totalRowCount = 0;
        //totalRowCount = resources.count();
        sprints.setRowCount(totalRowCount); 
        return sprints;
    }

    public void setSprint(LazyDataModel<Sprint> sprints) {
        this.sprints = sprints;
    }

    

    public Sprint getCurrentSprint() {
        return currentSprint;
    }

    public void setCurrentSprint(Sprint currentSprint) {
        this.currentSprint = currentSprint;
    }
    public String showMedicos(){
        setCurrentSprint(sprints.getRowData());
        return "/sprint/show.xhtml";
    }
    public String remove(){
        Sprint currentSprint =sprints.getRowData();
        //Remover currentSprint;        
        return "/sprint/show.xhtml";
    }
    public String create(){
        Sprint sprint = new Sprint();
        setCurrentSprint(sprint);
        return "/sprint/create.xhtml";
    }
    public String save(){
        if (currentSprint != null){
            //Almacenar currentSprint
        }
        return "/sprint/show.xhtml";
    }

    
}
