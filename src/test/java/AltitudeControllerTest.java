import com.sarobotics.hops.AltitudeController;
import org.junit.Test;
import static org.junit.Assert.*;

public class AltitudeControllerTest {

  private AltitudeController ac = new AltitudeController( 9 );

  @Test
  public void goingUP(){
    ac.settaAltitudineAttuale(10);
    Boolean goingUp = ac.isGoingUp();
    assertEquals(true, goingUp);
  }

  @Test
  public void goingDOWN(){
    ac.settaAltitudineAttuale(8);
    Boolean goingDown = ac.isGoingDown();
    assertEquals(true, goingDown);
  }

  @Test
  public void getActualAltitude(){
    int altitudine = 8;
    ac.settaAltitudineAttuale(altitudine);
    assertEquals(altitudine, ac.getActualAltitude(), 0 );
  }



}
