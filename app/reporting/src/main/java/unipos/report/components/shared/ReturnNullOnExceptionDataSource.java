package unipos.report.components.shared;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;
import org.apache.commons.beanutils.PropertyUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class ReturnNullOnExceptionDataSource extends JRAbstractBeanDataSource implements Serializable {

    private Collection data = null;
    private Iterator iterator = null;
    private Object currentBean = null;

    public ReturnNullOnExceptionDataSource(Object object) {
        super(true);

// Create an empty list 
        this.data = new LinkedList();

// Add the object to the list 
        this.data.add(object);

// Ensure the iterator is initialized 
        this.moveFirst();
    }

    public ReturnNullOnExceptionDataSource(Collection beanCollection) {
        super(true);

        this.data = beanCollection;

// Ensure the iterator is initialized 
        this.moveFirst();
    }

    public ReturnNullOnExceptionDataSource(Collection beanCollection, boolean isUseFieldDescription) {
        super(isUseFieldDescription);

        this.data = beanCollection;

// Ensure the iterator is initialized 
        this.moveFirst();
    }

    /**
     * Retreives the field with a certain name in the current bean. If the name is currentBean
     * then the current bean is returned
     */
    @Override
    public Object getFieldValue(JRField field) throws JRException {
        Object value = null;

        if (isCurrentBeanMapping(getPropertyName(field)))
        {
            value = currentBean;
        }
        else if (currentBean != null)
        {
            try
            {
                value = PropertyUtils.getProperty(currentBean, getPropertyName(field));
            }
            catch (Exception e) {
                value = null;
            }
        }

        return value;
    }

    /**
     * Get the next bean
     */
    public boolean next() throws JRException {
        boolean hasNext = false;

        if (this.iterator != null) {
            hasNext = this.iterator.hasNext();

            if (hasNext) {
                this.currentBean = this.iterator.next();
            }
        }

        return hasNext;
    }

    /**
     * Move the pointer to the first bean
     */
    public void moveFirst() {
        if (this.data != null) {
            this.iterator = this.data.iterator();
        }
    }
} 