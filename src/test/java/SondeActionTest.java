import com.sarobotics.hops.SondeActions;
import org.junit.Test;
import static  org.junit.Assert.*;

public class SondeActionTest {

  private SondeActions sa = new SondeActions(100, 70);

  @Test
  public void testSgancioSondaFalse(){
    boolean sgancia = sa.canBurst(100);
    assertEquals(false,sgancia);
  }

  @Test
  public void testSgancioSondaTrue(){
    boolean sgancia = sa.canBurst(101);
    assertEquals(true,sgancia);
  }


  @Test
  public void testAperturaParacaduteFalse(){
    boolean apriParacadute = sa.canOpenParachute(70);
    assertEquals(false,apriParacadute);
  }

  @Test
  public void testAperturaParacaduteTrue(){
    sa.setDeveAncoraScoppiare(false); //Significa che la sonda ha raggiunto il burst point.
    boolean apriParacadute = sa.canOpenParachute(69);
    assertEquals(true,apriParacadute);
  }




}
