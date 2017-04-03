package club.orchid.service

import club.orchid.domain.product.IProduct

/**
 * @author Dmitri Zaporozhtsev <dmitri.zera@gmail.com>
 */
public interface IProductService {
    List<IProduct> getAllProducts()
    IProduct getProduct(HashMap<String, Object> map)
    IProduct update(IProduct product)
    IProduct delete(IProduct product)
}
