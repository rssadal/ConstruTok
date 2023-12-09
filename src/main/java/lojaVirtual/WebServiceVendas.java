package lojaVirtual;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@WebService
public interface WebServiceVendas {
    
    @WebMethod
    @GET
    @Path("/compra/{nome}/{dinheiro}")
    @Produces(MediaType.TEXT_PLAIN)
	String compra(@PathParam(value = "nome") String nome, @PathParam(value = "dinheiro") double dinheiro, String usuario);
    
}
