package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class Handler implements RequestHandler<Map<String,String>, String>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(Map<String,String> event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    String response = "200 OK";
    logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.log("CONTEXT: " + gson.toJson(context));
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("EVENT TYPE: " + event.getClass());
    saveH2(logger);
    return response;
  }

  public void saveH2(LambdaLogger logger) {
    String url = "jdbc:h2:mem:";
    logger.log("EVENT: Saving in h2 ");
        try (Connection con = DriverManager.getConnection(url);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery("SELECT 1+1")) {
            if (rs.next()) {
                System.out.println(rs.getInt(1));
                logger.log("EVENT - Query executada com Sucesso! " + rs.getInt(1));
            }
        } catch (SQLException ex) {
            logger.log(ex.getMessage());
        }
  }
}
