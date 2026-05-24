package neurocode.beebetter.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shop_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer price;

    // Nome do asset local no Flutter: ex: "chapeu_coroa", "cor_azul"
    @Column(nullable = false)
    private String assetName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_item_id")
    private ShopItem shopItem;

    public enum ItemCategory {
        HAT, BEE_BODY, BEE_WINGS, HIVE, QUEEN_BODY, QUEEN_WINGS
    }
}