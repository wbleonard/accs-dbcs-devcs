package com.oracle.cloud.accs.sample.jpa.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JPA entity
 * 
 */
@Entity
@Table(name = "PAAS_APPDEV_PRODUCTS")
@XmlRootElement
public class PaasAppDev implements Serializable {

    @Id
    private String name;

    @Column(nullable = false)
    private String webUrl;

    public PaasAppDev() {
        //for JPA
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

}
