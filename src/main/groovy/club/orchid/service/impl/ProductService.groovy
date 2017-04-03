package club.orchid.service.impl

import club.orchid.dao.product.ProductRepository
import club.orchid.domain.product.IProduct
import club.orchid.service.IProductService
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.imageio.spi.ServiceRegistry

/**
 * @author Dmitri Zaporozhtsev <dmitri.zera@gmail.com>
 */
@Service
public class ProductService implements IProductService {
    private static final Logger log = Logger.getLogger(ProductService.class.name)

    @Autowired
    @Qualifier('productSolrRepository')
    private ProductRepository productRepository

    @Override
    List<IProduct> getAllProducts() {
        return productRepository.products();
    }

    @Override
    IProduct getProduct(HashMap<String, Object> map) {
        return productRepository.get(map)
    }

    @Override
    @Transactional
    IProduct update(IProduct product) {
        return productRepository.update(product)
    }

    @Override
    IProduct delete(IProduct product) {
        return null
    }
}
