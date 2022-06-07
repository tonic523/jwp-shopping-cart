package woowacourse.shoppingcart.application;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.product.Product;
import woowacourse.shoppingcart.domain.user.Customer;
import woowacourse.shoppingcart.dto.CartItemResponse;
import woowacourse.shoppingcart.exception.badrequest.InvalidProductException;
import woowacourse.shoppingcart.exception.NotInCustomerCartItemException;

import java.util.List;
import woowacourse.shoppingcart.exception.badrequest.DuplicateCartItemException;
import woowacourse.shoppingcart.exception.badrequest.InvalidProductIdException;
import woowacourse.shoppingcart.exception.unauthorized.UnauthorizedException;

@Service
@Transactional
public class CartService {

    private final CartItemDao cartItemDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;

    public CartService(final CartItemDao cartItemDao, final CustomerDao customerDao, final ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
    }

    public List<CartItemResponse> findCartsByCustomerEmail(final String email) {
        Long customerId = customerDao.findByEmail(email)
                .orElseThrow(UnauthorizedException::new)
                .getId();
        List<CartItem> cartItems = cartItemDao.findByCustomerId(customerId);
        return cartItems.stream()
                .map(cartItem -> new CartItemResponse(cartItem.getId(), cartItem.getName(), cartItem.getPrice(),
                        cartItem.getImageUrl(), cartItem.getQuantity()))
                .collect(Collectors.toList());
    }

    private List<Long> findCartIdsByCustomerName(final String email) {
        final Long customerId = customerDao.findByEmail(email)
                .orElseThrow(UnauthorizedException::new)
                .getId();
        return cartItemDao.findIdsByCustomerId(customerId);
    }

    public Long addCart(Long productId, String email) {
        Customer customer = customerDao.findByEmail(email)
                .orElseThrow(UnauthorizedException::new);
        Product product = productDao.findProductById(productId)
                .orElseThrow(InvalidProductIdException::new);
        if (cartItemDao.findProductIdsByCustomerId(customer.getId()).contains(product.getId())) {
            throw new DuplicateCartItemException();
        }
        try {
            return cartItemDao.addCartItem(customer.getId(), product.getId());
        } catch (Exception e) {
            throw new InvalidProductException();
        }
    }

    public void deleteCart(final String customerName, final Long cartId) {
        validateCustomerCart(cartId, customerName);
        cartItemDao.deleteCartItem(cartId);
    }

    private void validateCustomerCart(final Long cartId, final String customerName) {
        final List<Long> cartIds = findCartIdsByCustomerName(customerName);
        if (cartIds.contains(cartId)) {
            return;
        }
        throw new NotInCustomerCartItemException();
    }
}
