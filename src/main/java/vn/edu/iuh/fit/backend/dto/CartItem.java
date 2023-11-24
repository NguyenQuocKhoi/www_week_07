package vn.edu.iuh.fit.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.backend.models.Product;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
private Product product;
private int quantity;


}
