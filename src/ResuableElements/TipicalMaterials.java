package ResuableElements;

import Materials.BlingPhongMaterial;
import vectors.Vector3D;

public class TipicalMaterials {

    public static final BlingPhongMaterial GOLD = new BlingPhongMaterial(
            new Vector3D(255, 215, 0).getRGB(),
            0.1,
            0.5,
            0.9,
            64.0,
            0.0,
            0.5
    );

    public static final BlingPhongMaterial SILVER = new BlingPhongMaterial(
            new Vector3D(192, 192, 192).getRGB(),
            0.1,
            0.5,
            0.95,
            96,
            0.0,    // transparencia (opaco)
            0.9     // reflectividad alta por ser metal pulido
    );

    public static final BlingPhongMaterial BRONZE = new BlingPhongMaterial(
            new Vector3D(205, 127, 50).getRGB(),
            0.1,
            0.5,
            0.8,
            64,
            0.0,    // completamente opaco
            0.6     // reflectividad moderada
    );

    public static final BlingPhongMaterial PLASTIC_RED = new BlingPhongMaterial(
            new Vector3D(255, 0, 0).getRGB(),
            0.3,
            0.6,
            0.8,
            10,
            0.0,    // los plásticos sólidos no son transparentes
            0.3     // ligera reflectividad superficial
    );
    public static final BlingPhongMaterial PLASTIC_GREEN = new BlingPhongMaterial(
            new Vector3D(0, 255, 0).getRGB(),
            0.3,
            0.6,
            0.8,
            10,
            0.0,    // los plásticos sólidos no son transparentes
            0.3     // ligera reflectividad superficial
    );
    public static final BlingPhongMaterial PLASTIC_BLUE = new BlingPhongMaterial(
            new Vector3D(0,0, 255).getRGB(),
            0.3,
            0.6,
            0.8,
            10,
            0.0,    // los plásticos sólidos no son transparentes
            0.3     // ligera reflectividad superficial
    );

    public static final BlingPhongMaterial RUBBER_BLACK = new BlingPhongMaterial(
            new Vector3D(10, 10, 10).getRGB(),
            0.02,
            0.01,
            0.4,
            10,
            0.0,    // completamente opaco
            0.05    // prácticamente nada de reflectividad
    );

    public static final BlingPhongMaterial EMERALD = new BlingPhongMaterial(
            new Vector3D(80, 200, 120).getRGB(),
            0.2,
            0.2,
            1.0,
            256,
            0.3,    // algo de transparencia
            0.6     // brillo y reflejo internos típicos de una gema
    );

    public static final BlingPhongMaterial RUBY = new BlingPhongMaterial(
            new Vector3D(155, 17, 30).getRGB(),
            0.25,
            0.15,
            1.0,
            256,
            0.2,
            0.6
    );

    public static final BlingPhongMaterial SAPPHIRE = new BlingPhongMaterial(
            new Vector3D(15, 82, 186).getRGB(),
            0.25,
            0.2,
            1.0,
            256,
            0.2,
            0.6
    );

    public static final BlingPhongMaterial SAPPHIRE_2 = new BlingPhongMaterial(
            new Vector3D(15, 45, 150).getRGB(), // deeper, more vivid blue
            0.35,   // ambient - slightly more pronounced glow in shadow
            0.3,    // diffuse - makes it stand out under direct light
            1.0,    // specular - strong highlights
            512,    // shininess - crisper highlights (sharper reflections)
            0.15,   // reflectivity - reduced slightly for gemstone realism
            0.75    // transparency - more glass-like clarity
    );


    public static final BlingPhongMaterial DIAMOND = new BlingPhongMaterial(
            new Vector3D(230, 240, 255).getRGB(),
            0.3,
            0.1,
            1.0,
            512,
            0.7,    // alta transparencia
            0.9     // muy reflectante
    );

    public static final BlingPhongMaterial AMETHYST = new BlingPhongMaterial(
            new Vector3D(153, 102, 204).getRGB(),
            0.2,
            0.2,
            1.0,
            256,
            0.3,
            0.6
    );

    public static final BlingPhongMaterial MARBLE = new BlingPhongMaterial(
            new Vector3D(245, 245, 245).getRGB(),
            0.25,
            0.4,
            0.3,
            10,
            0.0,
            0.3
    );

    public static final BlingPhongMaterial MARBLE_WHITE = new BlingPhongMaterial(
            new Vector3D(245, 245, 245).getRGB(), // Blanco puro
            0.25,
            0.4,
            0.3,
            10,
            0.0,
            0.3
    );

    public static final BlingPhongMaterial MARBLE_BLACK = new BlingPhongMaterial(
            new Vector3D(30, 30, 30).getRGB(), // Mármol negro
            0.3,
            0.5,
            0.2,
            20,
            0.0,
            0.35
    );

    public static final BlingPhongMaterial MARBLE_GREEN = new BlingPhongMaterial(
            new Vector3D(34, 139, 102).getRGB(), // Verde mármol clásico
            0.28,
            0.45,
            0.25,
            15,
            0.0,
            0.3
    );

    public static final BlingPhongMaterial MARBLE_PINK = new BlingPhongMaterial(
            new Vector3D(240, 200, 210).getRGB(), // Mármol rosado claro
            0.27,
            0.42,
            0.3,
            12,
            0.0,
            0.3
    );

    public static final BlingPhongMaterial MARBLE_YELLOW = new BlingPhongMaterial(
            new Vector3D(240, 220, 170).getRGB(), // Tono amarillo-crema
            0.26,
            0.43,
            0.28,
            14,
            0.0,
            0.3
    );

    public static final BlingPhongMaterial MARBLE_OLD_WHITE = new BlingPhongMaterial(
            new Vector3D(230, 230, 230).getRGB(), // Blanco envejecido
            0.35,
            0.2,
            0.1,
            5,      // Menor brillo
            0.0,
            0.2     // Ligera rugosidad
    );

    public static final BlingPhongMaterial MARBLE_OLD_BEIGE = new BlingPhongMaterial(
            new Vector3D(210, 200, 180).getRGB(), // Beige apagado, estilo mármol antiguo
            0.33,
            0.18,
            0.1,
            4,
            0.0,
            0.25
    );

    public static final BlingPhongMaterial MARBLE_WORN_BEIGE = new BlingPhongMaterial(
            new Vector3D(180, 170, 150).getRGB(), // Beige más opaco y grisáceo
            0.28,   // Menos difusión (menos luz reflejada de forma suave)
            0.12,   // Brillo especular bajo
            0.05,   // Muy poco brillo
            2,      // Baja intensidad especular, casi sin puntos brillantes
            0.0,    // Opaco
            0.15    // Baja reflectividad, da apariencia envejecida
    );


    public static final BlingPhongMaterial MARBLE_OLD_GREEN = new BlingPhongMaterial(
            new Vector3D(85, 100, 90).getRGB(), // Verde musgoso opaco
            0.3,
            0.15,
            0.1,
            3,
            0.0,
            0.3
    );

    public static final BlingPhongMaterial MARBLE_OLD_ROSE = new BlingPhongMaterial(
            new Vector3D(200, 170, 170).getRGB(), // Rosado envejecido, casi terroso
            0.32,
            0.17,
            0.1,
            4,
            0.0,
            0.2
    );

    public static final BlingPhongMaterial SHINY_METAL = new BlingPhongMaterial(
            new Vector3D(220, 220, 220).getRGB(),
            0.1,
            0.3,
            1.0,
            256,
            0.0,
            0.9
    );

    public static final BlingPhongMaterial WOOD = new BlingPhongMaterial(
            new Vector3D(139, 69, 19).getRGB(),
            0.2,
            0.5,
            0.1,
            10,
            0.0,
            0.1
    );

    public static final BlingPhongMaterial DARK_WOOD = new BlingPhongMaterial(
            new Vector3D(100, 40, 10).getRGB(),
            0.2,
            0.5,
            0.1,
            10,
            0.0,
            0.1
    );

    public static final BlingPhongMaterial LEAF = new BlingPhongMaterial(
            new Vector3D(34, 139, 34).getRGB(),
            0.2,
            0.5,
            0.2,
            15,
            0.1,
            0.1
    );

    public static final BlingPhongMaterial DARK_LEAF = new BlingPhongMaterial(
            new Vector3D(0, 100, 0).getRGB(),   // Dark green
            0.2,
            0.4,
            0.2,
            10,
            0.05,
            0.1
    );

    public static final BlingPhongMaterial LIGHT_LEAF = new BlingPhongMaterial(
            new Vector3D(144, 238, 144).getRGB(), // Light green (like spring growth)
            0.3,
            0.6,
            0.3,
            20,
            0.05,
            0.15
    );

    public static final BlingPhongMaterial YELLOWISH_LEAF = new BlingPhongMaterial(
            new Vector3D(154, 205, 50).getRGB(),  // Yellow-green, like olive or aging leaves
            0.2,
            0.5,
            0.2,
            12,
            0.1,
            0.05
    );

    public static final BlingPhongMaterial WET_LEAF = new BlingPhongMaterial(
            new Vector3D(50, 205, 50).getRGB(),   // Bright green (wet or tropical leaf)
            0.4,
            0.7,
            0.3,
            40,
            0.2,
            0.2
    );

    public static final BlingPhongMaterial AUTUMN_LEAF = new BlingPhongMaterial(
            new Vector3D(210, 180, 140).getRGB(), // Tan/brownish for dried leaf
            0.15,
            0.3,
            0.1,
            5,
            0.05,
            0.05
    );


    public static final BlingPhongMaterial SAND = new BlingPhongMaterial(
            new Vector3D(194, 178, 128).getRGB(),
            0.25,
            0.4,
            0.1,
            5,
            0.0,
            0.05
    );

    public static final BlingPhongMaterial GLASS_CLEAR = new BlingPhongMaterial(
            new Vector3D(240, 255, 255).getRGB(),  // casi incoloro
            0.05,
            0.1,
            1.0,
            128,
            2,    // muy transparente
            0.1     // baja reflectividad
    );

    public static final BlingPhongMaterial GLASS_TINTED = new BlingPhongMaterial(
            new Vector3D(180, 255, 220).getRGB(),  // tinte verde-azulado
            0.05,
            0.1,
            1.0,
            128,
            0.8,    // alta transparencia
            0.3     // algo más reflectivo
    );

    public static final BlingPhongMaterial GLASS_FROSTED = new BlingPhongMaterial(
            new Vector3D(220, 220, 220).getRGB(),  // grisáceo (opaco)
            0.2,
            0.4,
            0.8,
            32,
            0.7,    // parcialmente transparente
            0.2     // reflectividad moderada
    );

    public static final BlingPhongMaterial GLASS_CHAMPAGNE = new BlingPhongMaterial(
            new Vector3D(255, 240, 200).getRGB(),  // dorado pálido
            0.05,
            0.15,
            1.0,
            96,
            0.85,   // muy transparente
            0.25    // brillo suave
    );

    public static final BlingPhongMaterial GLASS_BLUE = new BlingPhongMaterial(
            new Vector3D(180, 200, 255).getRGB(),  // azul pálido
            0.05,
            0.1,
            1.0,
            128,
            0.75,   // buena transparencia
            0.3     // algo reflectivo
    );

    public static final BlingPhongMaterial GLASS_AMBER = new BlingPhongMaterial(
            new Vector3D(255, 190, 120).getRGB(),  // ámbar cálido
            0.06,
            0.12,
            1.0,
            64,
            0.65,   // semi-transparente
            0.35    // bastante reflectivo
    );

    public static final BlingPhongMaterial GLASS_CRYSTAL = new BlingPhongMaterial(
            new Vector3D(255, 255, 255).getRGB(),  // blanco puro
            0.03,
            0.08,
            1.0,
            256,
            0.95,   // casi completamente transparente
            0.5     // reflejos nítidos
    );

    public static final BlingPhongMaterial GLASS_SMOKED = new BlingPhongMaterial(
            new Vector3D(120, 120, 120).getRGB(),  // gris oscuro
            0.1,
            0.2,
            1.0,
            64,
            0.5,    // medio transparente
            0.4     // reflectividad notable
    );

    public static final BlingPhongMaterial WATER = new BlingPhongMaterial(
            new Vector3D(100, 150, 200).getRGB(),  // azul claro
            0.05,   // ambiente tenue
            0.2,    // difuso moderado
            0.8,    // especular alto, pero no extremo
            32,     // brillo suave
            0.7,    // bastante transparente
            0.2     // baja reflectividad, como el agua real
    );

    // Agua cristalina (más clara y transparente)
    public static final BlingPhongMaterial CLEAR_WATER = new BlingPhongMaterial(
            new Vector3D(180, 220, 255).getRGB(),  // azul cielo claro
            0.05,   // ambiente tenue
            0.15,   // difuso bajo
            0.85,   // alto especular para reflejos nítidos
            64,     // brillo más fuerte
            0.9,    // muy transparente
            0.15    // baja reflectividad
    );

    // Agua profunda (más oscura y menos transparente)
    public static final BlingPhongMaterial DEEP_WATER = new BlingPhongMaterial(
            new Vector3D(20, 50, 100).getRGB(),  // azul marino profundo
            0.1,    // ambiente ligeramente más fuerte
            0.25,   // difuso medio
            0.6,    // especular moderado
            16,     // brillo más suave
            0.4,    // menos transparente
            0.25    // reflectividad moderada
    );

    // Agua tropical (color turquesa)
    public static final BlingPhongMaterial TROPICAL_WATER = new BlingPhongMaterial(
            new Vector3D(0, 210, 180).getRGB(),  // turquesa brillante
            0.04,   // ambiente tenue
            0.2,    // difuso moderado
            0.75,   // buen nivel especular
            48,     // brillo intermedio
            0.8,    // muy transparente
            0.18    // reflectividad baja
    );


    public static final BlingPhongMaterial PLANT_LEAF = new BlingPhongMaterial(
            new Vector3D(34, 139, 34).getRGB(),  // verde hoja
            0.1,
            0.5,
            0.2,
            8,
            0.0,    // opaco
            0.05    // ligera reflectividad natural
    );

    public static final BlingPhongMaterial YOUNG_SPROUT = new BlingPhongMaterial(
            new Vector3D(144, 238, 144).getRGB(),  // verde claro
            0.1,
            0.6,
            0.3,
            16,
            0.0,
            0.07
    );

    public static final BlingPhongMaterial CACTUS = new BlingPhongMaterial(
            new Vector3D(46, 100, 46).getRGB(),  // verde apagado
            0.05,
            0.4,
            0.1,
            4,
            0.0,
            0.02
    );

    public static final BlingPhongMaterial DRY_LEAF = new BlingPhongMaterial(
            new Vector3D(165, 42, 42).getRGB(),  // marrón rojizo
            0.05,
            0.4,
            0.05,
            2,
            0.0,
            0.01
    );

    public static final BlingPhongMaterial FLOWER_PETAL = new BlingPhongMaterial(
            new Vector3D(255, 105, 180).getRGB(),  // rosa fuerte
            0.1,
            0.5,
            0.4,
            16,
            0.0,
            0.1
    );

    public static final BlingPhongMaterial WET_MOSS = new BlingPhongMaterial(
            new Vector3D(30, 70, 30).getRGB(),  // verde húmedo
            0.08,
            0.4,
            0.3,
            12,
            0.0,
            0.1
    );


    public static final BlingPhongMaterial EVERGREEN_NEEDLE = new BlingPhongMaterial(
            new Vector3D(10, 50, 30).getRGB(),  // verde bosque
            0.03,
            0.25,
            0.1,
            4,
            0.0,
            0.02
    );

    public static final BlingPhongMaterial BAMBOO = new BlingPhongMaterial(
            new Vector3D(181, 201, 92).getRGB(),  // verde amarillento
            0.07,
            0.5,
            0.25,
            12,
            0.0,
            0.08
    );


    public static final BlingPhongMaterial CLOTH_COTTON_WHITE = new BlingPhongMaterial(
            new Vector3D(245, 245, 245).getRGB(), // Blanco suave
            0.4,    // Difuso alto
            0.3,    // Bajo especular
            0.1,    // Casi sin brillo
            5,      // Baja intensidad especular
            0.0,    // Opaco
            0.05    // Muy poca reflectividad
    );

    public static final BlingPhongMaterial CLOTH_DENIM_BLUE = new BlingPhongMaterial(
            new Vector3D(21, 54, 120).getRGB(), // Azul oscuro tipo mezclilla
            0.35,
            0.2,
            0.1,
            8,
            0.0,
            0.07
    );

    public static final BlingPhongMaterial CLOTH_WOOL_RED = new BlingPhongMaterial(
            new Vector3D(160, 30, 50).getRGB(), // Rojo oscuro y cálido
            0.45,
            0.2,
            0.1,
            6,
            0.0,
            0.05
    );

    public static final BlingPhongMaterial CLOTH_SILK_BLACK = new BlingPhongMaterial(
            new Vector3D(20, 20, 20).getRGB(), // Negro con cierto brillo
            0.25,
            0.5,
            0.6,
            32,     // Más brillo que otras telas
            0.0,
            0.2     // Un poco más reflectiva por la textura sedosa
    );

    public static final BlingPhongMaterial CLOTH_LINEN_BEIGE = new BlingPhongMaterial(
            new Vector3D(200, 180, 150).getRGB(), // Color lino
            0.4,
            0.2,
            0.1,
            5,
            0.0,
            0.05
    );

    public static final BlingPhongMaterial CLOTH_YELLOW = new BlingPhongMaterial(
            new Vector3D(255, 240, 100).getRGB(), // Amarillo suave, no neón
            0.45,   // Difuso alto
            0.25,   // Bajo especular
            0.1,    // Casi sin brillo
            6,      // Baja intensidad especular
            0.0,    // Completamente opaco
            0.05    // Muy poca reflectividad
    );


    // Pinturas (Paint-like materials)
    public static final BlingPhongMaterial PAINT_RED_GLOSSY = new BlingPhongMaterial(
            new Vector3D(200, 30, 30).getRGB(), // Rojo brillante
            0.4, 0.5, 0.6, 30, 0.0, 0.2
    );

    public static final BlingPhongMaterial PAINT_BLUE_MATTE = new BlingPhongMaterial(
            new Vector3D(40, 70, 160).getRGB(), // Azul mate
            0.55, 0.2, 0.1, 8, 0.0, 0.05
    );

    public static final BlingPhongMaterial PAINT_GREEN_SATIN = new BlingPhongMaterial(
            new Vector3D(60, 160, 90).getRGB(), // Verde satinado
            0.5, 0.35, 0.3, 20, 0.0, 0.1
    );

    public static final BlingPhongMaterial PAINT_WHITE_HIGHGLOSS = new BlingPhongMaterial(
            new Vector3D(250, 250, 250).getRGB(), // Blanco muy brillante
            0.35, 0.6, 0.8, 40, 0.0, 0.25
    );

    public static final BlingPhongMaterial PAINT_ORANGE_GLOSSY = new BlingPhongMaterial(
            new Vector3D(240, 120, 40).getRGB(), // Naranja intenso
            0.45, 0.5, 0.5, 28, 0.0, 0.18
    );

    public static final BlingPhongMaterial PAINT_PURPLE_MATTE = new BlingPhongMaterial(
            new Vector3D(120, 70, 160).getRGB(), // Púrpura apagado
            0.6, 0.2, 0.1, 6, 0.0, 0.03
    );

    // Concretos (Concrete-like materials)
    public static final BlingPhongMaterial CONCRETE_LIGHT_GRAY = new BlingPhongMaterial(
            new Vector3D(180, 180, 180).getRGB(), // Gris claro
            0.6, 0.1, 0.05, 5, 0.0, 0.02
    );

    public static final BlingPhongMaterial CONCRETE_DARK = new BlingPhongMaterial(
            new Vector3D(70, 70, 70).getRGB(), // Gris oscuro
            0.5, 0.15, 0.07, 7, 0.0, 0.01
    );

    public static final BlingPhongMaterial CONCRETE_BROWN = new BlingPhongMaterial(
            new Vector3D(100, 80, 60).getRGB(), // Marrón terroso
            0.55, 0.12, 0.05, 5, 0.0, 0.02
    );

    public static final BlingPhongMaterial CONCRETE_REDDISH = new BlingPhongMaterial(
            new Vector3D(140, 70, 60).getRGB(), // Concreto con tono rojizo
            0.5, 0.1, 0.04, 4, 0.0, 0.015
    );

    public static final BlingPhongMaterial CONCRETE_YELLOWISH = new BlingPhongMaterial(
            new Vector3D(200, 180, 90).getRGB(), // Concreto amarillento
            0.6, 0.1, 0.06, 6, 0.0, 0.015
    );

    // Pinturas en tonos blancos y azules

    public static final BlingPhongMaterial PAINT_WHITE_MATTE = new BlingPhongMaterial(
            new Vector3D(240, 240, 240).getRGB(), // Blanco mate
            0.6, 0.2, 0.1, 6, 0.0, 0.03
    );

    public static final BlingPhongMaterial PAINT_WHITE_GLOSSY = new BlingPhongMaterial(
            new Vector3D(255, 255, 255).getRGB(), // Blanco brillante
            0.35, 0.6, 0.8, 40, 0.0, 0.25
    );

    public static final BlingPhongMaterial PAINT_LIGHT_BLUE = new BlingPhongMaterial(
            new Vector3D(130, 180, 255).getRGB(), // Azul cielo claro
            0.5, 0.35, 0.4, 20, 0.0, 0.1
    );

    public static final BlingPhongMaterial PAINT_DEEP_BLUE = new BlingPhongMaterial(
            new Vector3D(30, 60, 150).getRGB(), // Azul profundo
            0.4, 0.5, 0.6, 28, 0.0, 0.18
    );

    public static final BlingPhongMaterial PAINT_CYAN = new BlingPhongMaterial(
            new Vector3D(50, 200, 210).getRGB(), // Azul turquesa (cian)
            0.5, 0.4, 0.5, 25, 0.0, 0.15
    );

// Concretos en tonos blancos y azules

    public static final BlingPhongMaterial CONCRETE_WHITE = new BlingPhongMaterial(
            new Vector3D(220, 220, 220).getRGB(), // Concreto blanco
            0.6, 0.1, 0.05, 5, 0.0, 0.02
    );

    public static final BlingPhongMaterial CONCRETE_OFFWHITE = new BlingPhongMaterial(
            new Vector3D(200, 200, 190).getRGB(), // Blanco hueso
            0.55, 0.12, 0.05, 6, 0.0, 0.015
    );

    public static final BlingPhongMaterial CONCRETE_BLUE_GRAY = new BlingPhongMaterial(
            new Vector3D(100, 120, 150).getRGB(), // Azul grisáceo
            0.5, 0.1, 0.05, 5, 0.0, 0.02
    );

    public static final BlingPhongMaterial CONCRETE_NAVY = new BlingPhongMaterial(
            new Vector3D(40, 50, 90).getRGB(), // Azul marino oscuro
            0.45, 0.1, 0.05, 4, 0.0, 0.015
    );

    public static final BlingPhongMaterial CONCRETE_CYAN_TINTED = new BlingPhongMaterial(
            new Vector3D(160, 200, 210).getRGB(), // Concreto con tinte azul claro
            0.55, 0.1, 0.06, 5, 0.0, 0.015
    );

    // Cuero (Leather-like materials)

    public static final BlingPhongMaterial LEATHER_BROWN = new BlingPhongMaterial(
            new Vector3D(100, 60, 30).getRGB(), // Marrón cuero clásico
            0.5, 0.3, 0.25, 15, 0.0, 0.08
    );

    public static final BlingPhongMaterial LEATHER_DARK_BROWN = new BlingPhongMaterial(
            new Vector3D(60, 40, 20).getRGB(), // Marrón oscuro
            0.45, 0.25, 0.2, 10, 0.0, 0.05
    );

    public static final BlingPhongMaterial LEATHER_RED = new BlingPhongMaterial(
            new Vector3D(120, 30, 30).getRGB(), // Rojo vino (cuero elegante)
            0.48, 0.32, 0.35, 18, 0.0, 0.1
    );

    public static final BlingPhongMaterial LEATHER_BLACK = new BlingPhongMaterial(
            new Vector3D(25, 25, 25).getRGB(), // Cuero negro
            0.4, 0.35, 0.4, 15, 0.0, 0.08
    );

    public static final BlingPhongMaterial LEATHER_TAN = new BlingPhongMaterial(
            new Vector3D(180, 130, 80).getRGB(), // Cuero claro tipo "tan"
            0.55, 0.3, 0.3, 16, 0.0, 0.07
    );

    public static final BlingPhongMaterial LEATHER_GRAY = new BlingPhongMaterial(
            new Vector3D(100, 100, 100).getRGB(), // Gris cuero moderno
            0.5, 0.25, 0.2, 12, 0.0, 0.06
    );

    public static final BlingPhongMaterial LEATHER_BLUE = new BlingPhongMaterial(
            new Vector3D(50, 70, 130).getRGB(), // Azul oscuro elegante
            0.48, 0.3, 0.3, 14, 0.0, 0.09
    );

}
