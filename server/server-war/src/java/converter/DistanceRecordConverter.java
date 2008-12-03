/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package converter;

import java.net.URI;
import persistence.Distance;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import java.util.Collection;
import persistence.Filter;

/**
 *
 * @author dave
 */

@XmlRootElement(name = "distance")
public class DistanceRecordConverter {
    private Distance entity;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of DistanceConverter */
    public DistanceRecordConverter() {
        entity = new Distance();
    }

    /**
     * Creates a new instance of DistanceConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public DistanceRecordConverter(Distance entity, URI uri, int expandLevel, boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
    }

    /**
     * Creates a new instance of DistanceConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public DistanceRecordConverter(Distance entity, URI uri, int expandLevel) {
        this(entity, uri, expandLevel, false);
    }

    /**
     * Getter for id.
     *
     * @return value for id
     */
    @XmlElement
    public String getId() {
        return (expandLevel > 0) ? entity.getId() : null;
    }

    /**
     * Setter for id.
     *
     * @param value the value to set
     */
    public void setId(String value) {
        entity.setId(value);
    }

    /**
     * Getter for bucket.
     *
     * @return value for bucket
     */
    @XmlElement
    public Short getBucket() {
        return (expandLevel > 0) ? entity.getBucket() : null;
    }

    /**
     * Setter for bucket.
     *
     * @param value the value to set
     */
    public void setBucket(Short value) {
        entity.setBucket(value);
    }

    @XmlAttribute
    public URI getUri() {
        return uri;
    }

    /**
     * Sets the URI for this reference converter.
     *
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Returns the Distance entity.
     *
     * @return an entity
     */
    @XmlTransient
    public Distance getEntity() {
        if (entity.getId() == null) {
            DistanceRecordConverter converter = UriResolver.getInstance().resolve(DistanceRecordConverter.class, uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved Distance entity.
     *
     * @return an resolved entity
     */
    public Distance resolveEntity(EntityManager em) {
        Collection<Filter> filterCollection = entity.getFilterCollection();
        Collection<Filter> newfilterCollection = new java.util.ArrayList<Filter>();
        for (Filter item : filterCollection) {
            newfilterCollection.add(em.getReference(Filter.class, item.getId()));
        }
        entity.setFilterCollection(newfilterCollection);
        return entity;
    }
}