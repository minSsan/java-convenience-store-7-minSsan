package store.service;

import store.domain.vo.Inventory;
import store.domain.Promotion;
import store.domain.vo.*;
import store.infrastructure.constant.Delimiter;
import store.repository.inventory.InventoryRepository;
import store.repository.product.ProductRepository;
import store.repository.promotion.PromotionRepository;
import store.service.vo.ProductTitle;
import store.service.vo.PromotionTitle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * 가게 제품, 프로모션, 재고 정보를 파일 입력으로 받아서 저장하는 서비스
 */
public class StoreFileInputService {
    private static final String PROMOTION_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCT_PATH = "src/main/resources/products.md";

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final InventoryRepository inventoryRepository;

    public StoreFileInputService(
            ProductRepository productRepository,
            PromotionRepository promotionRepository,
            InventoryRepository inventoryRepository
    ) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public void loadAndSave() {
        try {
            loadPromotion();
            loadProduct();
        } catch (IOException e) {
            throw new RuntimeException("파일을 로드하는 과정에서 오류가 발생했습니다.");
        }
    }

    private void loadPromotion() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(PROMOTION_PATH));
        EnumMap<PromotionTitle, Integer> indexInfo = getPromotionIndexInfo(reader.readLine());
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            List<String> split = Arrays.stream(line.split(Delimiter.FILE_INPUT_SPLIT_DELIMITER))
                    .map(String::trim).toList();
            validateColumnCount(split.size(), PromotionTitle.values().length);
            savePromotionLine(split, indexInfo);
        }
    }

    private void loadProduct() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_PATH));
        EnumMap<ProductTitle, Integer> indexInfo = getProductIndexInfo(reader.readLine());
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            List<String> split = Arrays.stream(line.split(Delimiter.FILE_INPUT_SPLIT_DELIMITER))
                    .map(String::trim).toList();
            validateColumnCount(split.size(), ProductTitle.values().length);
            saveProductLine(split, indexInfo);
        }
    }

    private EnumMap<PromotionTitle, Integer> getPromotionIndexInfo(String header) {
        EnumMap<PromotionTitle, Integer> result = new EnumMap<>(PromotionTitle.class);
        List<String> titles = Arrays.stream(header.split(Delimiter.FILE_INPUT_SPLIT_DELIMITER))
                .map(String::trim).distinct().toList();
        validateColumnCount(titles.size(), PromotionTitle.values().length);
        for (int i = 0; i < titles.size(); i++) {
            result.put(PromotionTitle.from(titles.get(i)), i);
        }
        return result;
    }

    private EnumMap<ProductTitle, Integer> getProductIndexInfo(String header) {
        EnumMap<ProductTitle, Integer> result = new EnumMap<>(ProductTitle.class);
        List<String> titles = Arrays.stream(header.split(Delimiter.FILE_INPUT_SPLIT_DELIMITER))
                .map(String::trim).distinct().toList();
        validateColumnCount(titles.size(), ProductTitle.values().length);
        for (int i = 0; i < titles.size(); i++) {
            result.put(ProductTitle.from(titles.get(i)), i);
        }
        return result;
    }

    private void validateColumnCount(int value, int target) {
        if (value != target) {
            throw new RuntimeException("파일 칼럼 명의 개수가 올바르지 않습니다.");
        }
    }

    private void savePromotionLine(List<String> line, EnumMap<PromotionTitle, Integer> indexInfo) {
        Name name = new Name(line.get(indexInfo.get(PromotionTitle.NAME)));
        Quantity get = Quantity.of(line.get(indexInfo.get(PromotionTitle.GET)));
        Quantity buy = Quantity.of(line.get(indexInfo.get(PromotionTitle.BUY)));
        String start = line.get(indexInfo.get(PromotionTitle.START_DATE));
        String end = line.get(indexInfo.get(PromotionTitle.END_DATE));
        Promotion promotion = new Promotion(name, buy, get, convertToLocalDate(start), convertToLocalDate(end));
        promotionRepository.save(promotion);
    }

    private void saveProductLine(List<String> line, EnumMap<ProductTitle, Integer> indexInfo) {
        Name name = new Name(line.get(indexInfo.get(ProductTitle.NAME)));
        Price price = Price.from(line.get(indexInfo.get(ProductTitle.PRICE)));
        Quantity quantity = Quantity.of(line.get(indexInfo.get(ProductTitle.QUANTITY)));
        Name promotionName = new Name(line.get(indexInfo.get(ProductTitle.PROMOTION)));
        Product product = new Product(name, price);

        saveInventory(product, quantity, promotionName);
        saveProduct(product, promotionName);
    }

    private void saveInventory(Product product, Quantity quantity, Name promotionName) {
        if (promotionName.equals("null")) {
            Inventory inventory = inventoryRepository.findByProductName(product.name()).sumNormal(quantity);
            inventoryRepository.save(product.name(), inventory);
            return;
        }
        validatePromotion(promotionName);
        Inventory inventory = inventoryRepository.findByProductName(product.name()).sumPromotion(quantity);
        inventoryRepository.save(product.name(), inventory);
    }

    private void saveProduct(Product product, Name promotionName) {
        if (promotionName.equals("null")) {
            final boolean isExist = productRepository.findByName(product.name()) != null;
            if (!isExist) {
                productRepository.save(product, null);
            }
            return;
        }
        validatePromotion(promotionName);
        Promotion promotion = promotionRepository.findByName(promotionName);
        productRepository.save(product, promotion);
    }

    private void validatePromotion(Name promotionName) {
        Promotion promotion = promotionRepository.findByName(promotionName);
        if (promotion == null) {
            throw new RuntimeException("올바르지 않은 프로모션 명이 존재합니다.");
        }
    }

    private LocalDate convertToLocalDate(String input) {
        List<String> split = Arrays.stream(input.split(Delimiter.FILE_INPUT_DATE_DELIMITER))
                .map(String::trim).toList();
        if (split.size() != 3) {
            throw new RuntimeException("파일 내용의 날짜 형식이 올바르지 않습니다.");
        }
        int year = Integer.parseInt(split.getFirst());
        int month = Integer.parseInt(split.get(1));
        int day = Integer.parseInt(split.get(2));
        return LocalDate.of(year, month, day);
    }
}
