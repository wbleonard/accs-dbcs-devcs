package com.oracle.cloud.accs.sample.rest;

import com.oracle.cloud.accs.sample.jpa.entity.PaasAppDev;
import com.oracle.cloud.accs.sample.jpa.JPAFacade;
import java.util.List;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 * Provides REST interface for clients to work with JSRs. Leverages
 * JPARepository class
 *
 */
@Path("appdev/products")
public class PaasAppDevProductsResource {

    @GET
    @Path("{name}")
    public Response paasOffering(@PathParam("name") String name) {

        EntityManager em = null;
        PaasAppDev product = null;
        try {
            em = JPAFacade.getEM();
            product = em.find(PaasAppDev.class, name);
        } catch (Exception e) {
            throw e;
        } finally {

            if (em != null) {
                em.close();
            }

        }
        
        return Response.ok(product).build();
    }
    
    @GET
    public Response all() {

        EntityManager em = null;
        List<PaasAppDev> products = null;
        try {
            em = JPAFacade.getEM();
            products = em.createQuery("SELECT c FROM PaasAppDev c").getResultList();
        } catch (Exception e) {
            throw e;
        } finally {

            if (em != null) {
                em.close();
            }

        }
        GenericEntity<List<PaasAppDev>> list = new GenericEntity<List<PaasAppDev>>(products) {
        };
        return Response.ok(list).build();
    }

}
