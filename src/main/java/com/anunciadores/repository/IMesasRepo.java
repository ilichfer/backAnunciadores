package  com.anunciadores.repository;
import com.anunciadores.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IMesasRepo extends JpaRepository<Mesa, Integer> {}
