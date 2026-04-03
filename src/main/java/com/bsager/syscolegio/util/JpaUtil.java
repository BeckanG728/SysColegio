package com.bsager.syscolegio.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author
 */
public class JpaUtil {

    // Nombre de la unidad de persistencia definida en persistence.xml
    private static final String PU = "SysColegioPU";

    // La factory se crea una sola vez al cargar la clase
    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory(PU);

    // Constructor privado: nadie puede instanciar JpaUtil
    private JpaUtil() {
    }

    /**
     * Devuelve la unica instancia del EntityManagerFactory.Uso: 
 new AlumnoJpaController(JpaUtil.getFactory())
     * @return 
     */
    public static EntityManagerFactory getFactory() {
        return FACTORY;
    }

    /**
     * Cierra el factory al apagar la aplicacion. Llamar desde un
     * ServletContextListener en contextDestroyed().
     */
    public static void shutdown() {
        if (FACTORY != null && FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
