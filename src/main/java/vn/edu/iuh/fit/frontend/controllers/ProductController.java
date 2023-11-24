package vn.edu.iuh.fit.frontend.controllers;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.backend.dto.CartItem;
import vn.edu.iuh.fit.backend.models.Employee;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.repositories.ProductRepository;
import vn.edu.iuh.fit.backend.services.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductController {

  @Autowired
  private ProductService productServices;
  @Autowired
  private ProductRepository productRepository;

  @GetMapping("/products")
  public String showProductListPaging(Model model,
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(5);

    Page<Product> productPage = productServices.findPaginated(currentPage - 1,
        pageSize, "name", "asc");

    model.addAttribute("productPage", productPage);

    int totalPages = productPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
          .boxed()
          .collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    return "admin/product/listing";
  }

  @GetMapping("/show-add-form")
  public String add(Model model) {
    Product product = new Product();
    model.addAttribute("product", product);
    return "admin/product/add-form";
  }

  @PostMapping("/products/add")
  public String addProduct(
      @ModelAttribute("product") Product product,
      BindingResult result, Model model) {
    productRepository.save(product);
    return "redirect:/products";
  }

  //    @DeleteMapping("/products/delete/{id}")
  @GetMapping("/products/delete/{id}")
  public String deleteProduct(@PathVariable("id") long id) {
    Product product = productRepository.findById(id).orElse(new Product());
    productRepository.delete(product);
    return "redirect:/products";
  }
  @GetMapping("/products/show-edit-form/{id}")
  public String showUpdateForm(@PathVariable("id") long id, Model model) {
    Product product = productRepository.findById(id).orElse(null);
    model.addAttribute("product", product);
    return "admin/product/editProduct";
  }

  @PostMapping("admin/product/editProduct")
  public String update(
      @ModelAttribute("product") Product product,
      BindingResult result, Model model) {

  productRepository.save(product);
    return "redirect:/products";
  }

  @GetMapping("/home")
  public String home(Model model, HttpSession session) {
    List<Product> products = productRepository.findAll();
    model.addAttribute("products", products);
    return "client/home";
  }

  @GetMapping("/buy/{id}")
  public String buy(Model model, HttpSession session,
      @PathVariable("id") long id) {

    Object obj = session.getAttribute("cart");
//    ArrayList<CartItem> cartItems = null;
    Map<Long, CartItem> cartItemMap = null;
    if (obj == null) {
      cartItemMap = new HashMap<>(); //tao gio rong
    } else {
      cartItemMap = (HashMap<Long, CartItem>) obj; //lay gio da mua truoc do
    }
//mua hang
    Product selProduct = productRepository.findById(id).get();

    int quan = cartItemMap.get(selProduct.getProduct_id()) == null ? 1 //chua mua san pham nay
        : cartItemMap.get(selProduct.getProduct_id()).getQuantity() + 1; //da san pham nay --> tang so luong
    CartItem item = new CartItem(selProduct, quan);

    cartItemMap.put(selProduct.getProduct_id(), item);
    //them vao session (gio)
    session.setAttribute("cart", cartItemMap);
    return "redirect:/home";
  }

  @GetMapping("/checkout")
  public String checkout(HttpSession session, Model model){
    Object obj = session.getAttribute("cart");
    if(obj == null)
      return "/home";

    HashMap<Long, CartItem> cart = (HashMap<Long, CartItem>) obj;
    // dua vao model sau do chuyen sang tran tiep theo
    List<CartItem> list = new ArrayList<>(cart.values());
    model.addAttribute("selProduct", cart);

    return "client/checkout";
  }
}