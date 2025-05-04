package com.app.periodicpro

import android.app.Activity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.app.periodicpro.data.Element
import com.app.periodicpro.data.ElementCategory

class ElementDetailsActivity : Activity() {

    companion object {
        const val EXTRA_ELEMENT_ATOMIC_NUMBER = "element_atomic_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_element_details)

        // Get atomic number from intent
        val atomicNumber = intent.getIntExtra(EXTRA_ELEMENT_ATOMIC_NUMBER, 1)
        
        // Debug toast
        Toast.makeText(this, "Received element atomic number: $atomicNumber", Toast.LENGTH_SHORT).show()
        
        // Find element in our detailed dataset first
        var element = getAllElements().find { it.atomicNumber == atomicNumber }
        
        // If not found in our detailed list, try to find basic info from the periodic table data

        // Display the element details if found
        element?.let {
            displayElementDetails(it)
        } ?: run { 
            Toast.makeText(this, "Element with atomic number $atomicNumber not found", Toast.LENGTH_LONG).show()
            finish() // Close activity if element not found
        }
    }
    
    private fun displayElementDetails(element: Element) {
        // Set element symbol
        val symbolView = findViewById<TextView>(R.id.element_symbol)
        symbolView.text = element.symbol
        
        // Set background color based on category
        val bgResId = getCategoryBackgroundResource(element.category)
        symbolView.setBackgroundResource(bgResId)
        
        // Set element name and category
        findViewById<TextView>(R.id.element_name).text = element.name
        findViewById<TextView>(R.id.element_category).text = 
            element.category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
        
        // Basic properties
        findViewById<TextView>(R.id.atomic_number).text = element.atomicNumber.toString()
        findViewById<TextView>(R.id.atomic_mass).text = element.atomicMass.toString()
        findViewById<TextView>(R.id.element_position).text = "Row ${element.row}, Column ${element.column}"
        
        // Physical properties
        findViewById<TextView>(R.id.density).text = 
            if (element.density > 0) "${element.density} g/cm³" else "Unknown"
        findViewById<TextView>(R.id.melting_point).text = 
            if (element.meltingPoint > 0) "${element.meltingPoint} K" else "Unknown"
        findViewById<TextView>(R.id.boiling_point).text = 
            if (element.boilingPoint > 0) "${element.boilingPoint} K" else "Unknown"
        
        // Electronic properties
        findViewById<TextView>(R.id.electron_config).text = 
            if (element.electronConfiguration.isNotEmpty()) element.electronConfiguration else "Data not available"
        findViewById<TextView>(R.id.oxidation_states).text = 
            if (element.oxidationStates.isNotEmpty()) element.oxidationStates else "Data not available"
        
        // Historical information
        findViewById<TextView>(R.id.year_discovered).text = 
            if (element.yearDiscovered.isNotEmpty()) element.yearDiscovered else "Data not available"
        findViewById<TextView>(R.id.discoverer).text = 
            if (element.discoverer.isNotEmpty()) element.discoverer else "Data not available"
        
        // Applications
        findViewById<TextView>(R.id.applications).text = 
            if (element.applications.isNotEmpty()) element.applications else "Data not available"
        
        // Back button
        findViewById<ImageButton>(R.id.button_back).setOnClickListener {
            finish()
        }
    }
    
    private fun getCategoryBackgroundResource(category: ElementCategory): Int {
        return when (category) {
            ElementCategory.ALKALI_METAL -> R.drawable.rounded_alkali_metal_bg
            ElementCategory.ALKALINE_EARTH_METAL -> R.drawable.rounded_alkaline_earth_bg
            ElementCategory.TRANSITION_METAL -> R.drawable.rounded_transition_bg
            ElementCategory.POST_TRANSITION_METAL -> R.drawable.rounded_post_transition_bg
            ElementCategory.METALLOID -> R.drawable.rounded_metalloid_bg
            ElementCategory.NONMETAL -> R.drawable.rounded_nonmetal_bg
            ElementCategory.HALOGEN -> R.drawable.rounded_halogen_bg
            ElementCategory.NOBLE_GAS -> R.drawable.rounded_noble_gas_bg
            ElementCategory.LANTHANIDE -> R.drawable.rounded_lanthanide_bg
            ElementCategory.ACTINIDE -> R.drawable.rounded_actinide_bg
        }
    }
    

    
    private fun getAllElements(): List<Element> {
        // This provides basic information for all 118 elements in the periodic table
        return listOf(
            // Period 1
            Element(1, "H", "Hydrogen", 1, 1, ElementCategory.NONMETAL, 1.008, "1s¹",
                    density = 0.0899, meltingPoint = 14.01, boilingPoint = 20.28,
                    oxidationStates = "+1, -1",
                    yearDiscovered = "1766", discoverer = "Henry Cavendish",
                    applications = "Hydrogen is used in the production of ammonia for fertilizers, in methanol production, as a hydrogenating agent, in fuel cells for clean energy, and has potential as a sustainable fuel source. It occurs naturally in water and all organic compounds."),
            Element(2, "He", "Helium", 1, 18, ElementCategory.NOBLE_GAS, 4.0026, "1s²",
                    density = 0.1785, meltingPoint = 0.95, boilingPoint = 4.22,
                    oxidationStates = "0",
                    yearDiscovered = "1868", discoverer = "Pierre Janssen & Norman Lockyer",
                    applications = "Helium is used in cryogenics, pressurizing and purging systems, welding, maintaining controlled atmospheres, and in balloons and airships. It occurs naturally in natural gas deposits and in the sun and other stars."),
            
            // Period 2
            Element(3, "Li", "Lithium", 2, 1, ElementCategory.ALKALI_METAL, 6.94, "1s² 2s¹",
                    density = 0.534, meltingPoint = 453.69, boilingPoint = 1615.0,
                    oxidationStates = "+1",
                    yearDiscovered = "1817", discoverer = "Johan August Arfwedson",
                    applications = "Lithium is used in rechargeable batteries for mobile phones, laptops, and electric vehicles. It's also used in psychiatric medications for bipolar disorder, ceramics, glass, and as a lubricant. It naturally occurs in mineral springs and in some igneous rocks."),
            Element(4, "Be", "Beryllium", 2, 2, ElementCategory.ALKALINE_EARTH_METAL, 9.0122, "1s² 2s²",
                    density = 1.85, meltingPoint = 1560.0, boilingPoint = 2742.0,
                    oxidationStates = "+2",
                    yearDiscovered = "1798", discoverer = "Louis-Nicolas Vauquelin",
                    applications = "Beryllium is used in aerospace components, nuclear reactors, X-ray equipment, and computer parts due to its lightweight yet strong properties. It naturally occurs in about 30 minerals, including beryl and chrysoberyl, but is rare."),
            Element(5, "B", "Boron", 2, 13, ElementCategory.METALLOID, 10.81, "1s² 2s² 2p¹",
                    density = 2.34, meltingPoint = 2349.0, boilingPoint = 4200.0,
                    oxidationStates = "+3",
                    yearDiscovered = "1808", discoverer = "Joseph Louis Gay-Lussac & Louis Jacques Thénard",
                    applications = "Boron is used in borosilicate glass, detergents, semiconductors, magnets, and as a neutron absorber in nuclear reactors. It naturally occurs in borax, boric acid, and some mineral springs; it's essential for plant growth."),
            Element(6, "C", "Carbon", 2, 14, ElementCategory.NONMETAL, 12.011, "1s² 2s² 2p²",
                    density = 2.267, meltingPoint = 3823.0, boilingPoint = 4300.0,
                    oxidationStates = "+4, +2, -4",
                    yearDiscovered = "3750 BC", discoverer = "Ancient civilizations",
                    applications = "Carbon forms the basis of all organic chemistry and life. It's used in steel production, as graphite in pencils and lubricants, as diamonds in jewelry and cutting tools, in activated charcoal filters, in carbon fiber materials, and in nanotechnology (graphene, carbon nanotubes). It occurs in all living organisms, in fossil fuels, and as CO₂ in the atmosphere."),
            Element(7, "N", "Nitrogen", 2, 15, ElementCategory.NONMETAL, 14.007, "1s² 2s² 2p³",
                    density = 1.251, meltingPoint = 63.15, boilingPoint = 77.36,
                    oxidationStates = "+5, +4, +3, +2, +1, -1, -2, -3",
                    yearDiscovered = "1772", discoverer = "Daniel Rutherford",
                    applications = "Nitrogen is used in fertilizer production, as a refrigerant in cryogenics, in food packaging to prevent oxidation, and as an inert atmosphere for chemical reactions and food preservation. It makes up 78% of Earth's atmosphere and is a key component of proteins and DNA."),
            Element(8, "O", "Oxygen", 2, 16, ElementCategory.NONMETAL, 15.999, "1s² 2s² 2p⁴",
                    density = 1.429, meltingPoint = 54.36, boilingPoint = 90.2,
                    oxidationStates = "-2, -1",
                    yearDiscovered = "1774", discoverer = "Joseph Priestley & Carl Wilhelm Scheele",
                    applications = "Oxygen is essential for respiration and combustion. It's used in steel production, medical applications for patients with breathing problems, water treatment, and rocket propellants. It makes up 21% of Earth's atmosphere, is abundant in the earth's crust, and is essential for nearly all living organisms."),
            Element(9, "F", "Fluorine", 2, 17, ElementCategory.HALOGEN, 18.998, "1s² 2s² 2p⁵",
                    density = 1.696, meltingPoint = 53.53, boilingPoint = 85.03,
                    oxidationStates = "-1",
                    yearDiscovered = "1886", discoverer = "Henri Moissan",
                    applications = "Fluorine is used in toothpaste to prevent tooth decay, in fluoridated water, refrigerants, pharmaceuticals, and in the production of many materials like Teflon (non-stick coatings). It naturally occurs in minerals like fluorite and fluorapatite, and in some foods and water sources in trace amounts."),
            Element(10, "Ne", "Neon", 2, 18, ElementCategory.NOBLE_GAS, 20.180, "1s² 2s² 2p⁶",
                    density = 0.9, meltingPoint = 24.56, boilingPoint = 27.07,
                    oxidationStates = "0",
                    yearDiscovered = "1898", discoverer = "William Ramsay & Morris Travers",
                    applications = "Neon is used in neon signs, producing the distinctive red-orange glow, in lasers, high-voltage indicators, lightning arrestors, and as a cryogenic refrigerant. It's one of the rarest elements in Earth's atmosphere, making up only 0.0018% of the air we breathe."),
            
            // Period 3
            Element(11, "Na", "Sodium", 3, 1, ElementCategory.ALKALI_METAL, 22.990, "1s² 2s² 2p⁶ 3s¹",
                    density = 0.971, meltingPoint = 370.87, boilingPoint = 1156.0,
                    oxidationStates = "+1",
                    yearDiscovered = "1807", discoverer = "Humphry Davy",
                    applications = "Sodium is used in street lights, table salt (NaCl), and various industrial processes for chemical production and manufacturing. It's essential for nerve and muscle function in humans and animals, and is abundant in the Earth's oceans and in many minerals."),
            Element(12, "Mg", "Magnesium", 3, 2, ElementCategory.ALKALINE_EARTH_METAL, 24.305, "1s² 2s² 2p⁶ 3s²",
                    density = 1.738, meltingPoint = 923.0, boilingPoint = 1363.0,
                    oxidationStates = "+2",
                    yearDiscovered = "1808", discoverer = "Humphry Davy",
                    applications = "Magnesium is used in lightweight alloys for aerospace and automotive applications, fireworks, flash photography, and as a reducing agent in chemical reactions. It's essential for plant photosynthesis and human metabolism, and is found in many minerals and seawater."),
            Element(13, "Al", "Aluminum", 3, 13, ElementCategory.POST_TRANSITION_METAL, 26.982, "1s² 2s² 2p⁶ 3s² 3p¹",
                    density = 2.7, meltingPoint = 933.47, boilingPoint = 2792.0,
                    oxidationStates = "+3",
                    yearDiscovered = "1827", discoverer = "Friedrich Wöhler",
                    applications = "Aluminum is used in packaging, construction, transportation, electrical wiring, and household items due to its lightweight and corrosion-resistant properties. It's the most abundant metal in Earth's crust, found in minerals like bauxite, and is 100% recyclable."),
            Element(14, "Si", "Silicon", 3, 14, ElementCategory.METALLOID, 28.085, "1s² 2s² 2p⁶ 3s² 3p²",
                    density = 2.33, meltingPoint = 1687.0, boilingPoint = 3538.0,
                    oxidationStates = "+4, -4",
                    yearDiscovered = "1824", discoverer = "Jöns Jacob Berzelius",
                    applications = "Silicon is the basis of semiconductors and integrated circuits, making it essential for electronics, computers, and the digital age. It's also used in solar cells, glass, ceramics, silicone products, and construction materials. After oxygen, it's the second most abundant element in Earth's crust, found in sand, quartz, and many rocks."),
            Element(15, "P", "Phosphorus", 3, 15, ElementCategory.NONMETAL, 30.974, "1s² 2s² 2p⁶ 3s² 3p³",
                    density = 1.82, meltingPoint = 317.3, boilingPoint = 553.6,
                    oxidationStates = "+5, +3, -3",
                    yearDiscovered = "1669", discoverer = "Hennig Brand",
                    applications = "Phosphorus is essential for life, found in DNA, RNA, ATP, and bone structure. It's used in fertilizers to enhance plant growth, detergents, pesticides, matches, and fireworks. It occurs naturally in phosphate rocks, as well as in all living cells, particularly concentrated in bones and teeth."),
            Element(16, "S", "Sulfur", 3, 16, ElementCategory.NONMETAL, 32.06, "1s² 2s² 2p⁶ 3s² 3p⁴",
                    density = 2.07, meltingPoint = 388.36, boilingPoint = 717.87,
                    oxidationStates = "+6, +4, +2, -2",
                    yearDiscovered = "Ancient", discoverer = "Ancient civilizations",
                    applications = "Sulfur is used in the production of sulfuric acid (used in batteries), fertilizers, gunpowder, matches, insecticides, and in vulcanizing rubber to improve durability. It occurs naturally in volcanic regions, hot springs, and as sulfide and sulfate minerals, and is present in all living things as an essential component of proteins."),
            Element(17, "Cl", "Chlorine", 3, 17, ElementCategory.HALOGEN, 35.45, "1s² 2s² 2p⁶ 3s² 3p⁵",
                    density = 3.214, meltingPoint = 171.6, boilingPoint = 239.11,
                    oxidationStates = "+7, +5, +3, +1, -1",
                    yearDiscovered = "1774", discoverer = "Carl Wilhelm Scheele",
                    applications = "Chlorine is used in water purification, bleaches, disinfectants, and in the production of many chemicals, plastics (PVC), solvents, and pharmaceuticals. It's naturally found in many minerals, especially common salt (NaCl), and in seawater, where it's the second most abundant dissolved element after sodium."),
            Element(18, "Ar", "Argon", 3, 18, ElementCategory.NOBLE_GAS, 39.948, "1s² 2s² 2p⁶ 3s² 3p⁶",
                    density = 1.784, meltingPoint = 83.8, boilingPoint = 87.3,
                    oxidationStates = "0",
                    yearDiscovered = "1894", discoverer = "Lord Rayleigh & William Ramsay",
                    applications = "Argon is used in fluorescent lights, as a shielding gas in welding to prevent oxidation, in the production of titanium and other reactive metals, and to provide an inert atmosphere for scientific experiments. It makes up about 0.93% of Earth's atmosphere, making it the third most abundant gas after nitrogen and oxygen."),
            
            // Period 4 
            Element(19, "K", "Potassium", 4, 1, ElementCategory.ALKALI_METAL, 39.098, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s¹",
                    density = 0.862, meltingPoint = 336.53, boilingPoint = 1032.0,
                    oxidationStates = "+1",
                    yearDiscovered = "1807", discoverer = "Humphry Davy",
                    applications = "Potassium is essential for plant and animal life, particularly for nerve function and muscle control. It's used in fertilizers to promote plant growth, in soaps, and various chemical processes. It's found in many minerals, especially sylvite and carnallite, and is the 7th most abundant element in Earth's crust."),
            Element(20, "Ca", "Calcium", 4, 2, ElementCategory.ALKALINE_EARTH_METAL, 40.078, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s²",
                    density = 1.54, meltingPoint = 1115.0, boilingPoint = 1757.0,
                    oxidationStates = "+2",
                    yearDiscovered = "1808", discoverer = "Humphry Davy",
                    applications = "Calcium is essential for bones and teeth formation, muscle function, and nerve transmission. It's used in cement, plaster, and various metallurgical processes, as well as in the food industry as a supplement. It's the 5th most abundant element in Earth's crust, found in limestone, chalk, gypsum, and many other minerals."),
            
            // Continue with the remaining elements with basic information
            Element(21, "Sc", "Scandium", 4, 3, ElementCategory.TRANSITION_METAL, 44.956, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹",
                    density = 2.99, meltingPoint = 1814.0, boilingPoint = 3109.0,
                    oxidationStates = "+3",
                    yearDiscovered = "1879", discoverer = "Lars Fredrik Nilson",
                    applications = "Scandium is used in aluminum alloys for aerospace components, sporting goods like baseball bats and bicycle frames, and in high-intensity lamps. It's found in trace amounts in over 800 minerals, but is relatively rare and difficult to extract."),
            Element(22, "Ti", "Titanium", 4, 4, ElementCategory.TRANSITION_METAL, 47.867, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d²",
                    density = 4.5, meltingPoint = 1941.0, boilingPoint = 3560.0,
                    oxidationStates = "+4, +3, +2",
                    yearDiscovered = "1791", discoverer = "William Gregor",
                    applications = "Titanium is used in aerospace applications, military equipment, medical implants, eyeglass frames, jewelry, and sports equipment due to its high strength-to-weight ratio, corrosion resistance, and biocompatibility. It naturally occurs in minerals like rutile and ilmenite."),
            Element(23, "V", "Vanadium", 4, 5, ElementCategory.TRANSITION_METAL, 50.942, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d³",
                    density = 6.11, meltingPoint = 2183.0, boilingPoint = 3680.0,
                    oxidationStates = "+5, +4, +3, +2",
                    yearDiscovered = "1801", discoverer = "Andrés Manuel del Río",
                    applications = "Vanadium is primarily used in steel alloys to increase strength, toughness, and corrosion resistance. It's also used in aerospace applications, nuclear reactors, and as a catalyst in chemical production. It naturally occurs in about 65 minerals including vanadinite and carnotite."),
            Element(24, "Cr", "Chromium", 4, 6, ElementCategory.TRANSITION_METAL, 51.996, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s¹ 3d⁵",
                    density = 7.15, meltingPoint = 2180.0, boilingPoint = 2944.0,
                    oxidationStates = "+6, +3, +2",
                    yearDiscovered = "1797", discoverer = "Louis Nicolas Vauquelin",
                    applications = "Chromium is used in stainless steel production, chrome plating for corrosion resistance, dyes and pigments, leather tanning, and wood preservatives. It's an essential trace element for human health, involved in glucose metabolism. It's found in the mineral chromite."),
            Element(25, "Mn", "Manganese", 4, 7, ElementCategory.TRANSITION_METAL, 54.938, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d⁵",
                    density = 7.44, meltingPoint = 1519.0, boilingPoint = 2334.0,
                    oxidationStates = "+7, +6, +4, +3, +2",
                    yearDiscovered = "1774", discoverer = "Johan Gottlieb Gahn",
                    applications = "Manganese is used in steel production to increase hardness and resistance, in aluminum alloys, as a colorant in glass (giving it a purple color), and in batteries. It's essential for plant photosynthesis and animal metabolism. It's commonly found in minerals like pyrolusite and is abundant in Earth's crust."),
            Element(26, "Fe", "Iron", 4, 8, ElementCategory.TRANSITION_METAL, 55.845, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d⁶",
                    density = 7.874, meltingPoint = 1811.0, boilingPoint = 3134.0,
                    oxidationStates = "+3, +2",
                    yearDiscovered = "5000 BC", discoverer = "Ancient civilizations",
                    applications = "Iron is the most widely used metal, crucial for steel production which is used in construction, vehicles, machinery, tools, and countless other applications. It's essential for hemoglobin in blood which transports oxygen throughout the body. Iron is the fourth most abundant element in Earth's crust, primarily found in minerals like hematite and magnetite."),
            Element(27, "Co", "Cobalt", 4, 9, ElementCategory.TRANSITION_METAL, 58.933, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d⁷",
                    density = 8.9, meltingPoint = 1768.0, boilingPoint = 3200.0,
                    oxidationStates = "+3, +2",
                    yearDiscovered = "1735", discoverer = "Georg Brandt",
                    applications = "Cobalt is used in superalloys for gas turbine engines, magnetic alloys, hard metals for cutting tools, rechargeable batteries, and as a blue colorant in glass, ceramics, and paints. It's essential for humans as vitamin B12, and is found in minerals like cobaltite and erythrite."),
            Element(28, "Ni", "Nickel", 4, 10, ElementCategory.TRANSITION_METAL, 58.693, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d⁸",
                    density = 8.908, meltingPoint = 1728.0, boilingPoint = 3186.0,
                    oxidationStates = "+3, +2",
                    yearDiscovered = "1751", discoverer = "Axel Fredrik Cronstedt",
                    applications = "Nickel is used in stainless steel and other corrosion-resistant alloys, rechargeable batteries, coins, electroplating, and as a catalyst in chemical reactions. It can be found in Earth's crust in minerals like pentlandite and garnierite, as well as in meteorites."),
            Element(29, "Cu", "Copper", 4, 11, ElementCategory.TRANSITION_METAL, 63.546, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s¹ 3d¹⁰",
                    density = 8.96, meltingPoint = 1357.77, boilingPoint = 2835.0,
                    oxidationStates = "+2, +1",
                    yearDiscovered = "9000 BC", discoverer = "Ancient civilizations",
                    applications = "Copper is used in electrical wiring due to its excellent conductivity, plumbing, roofing, currency, cookware, and as an antimicrobial agent. It's essential for human metabolism, particularly in the formation of red blood cells and maintaining nerve cells. It occurs naturally in minerals like chalcopyrite and bornite."),
            Element(30, "Zn", "Zinc", 4, 12, ElementCategory.TRANSITION_METAL, 65.38, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰",
                    density = 7.14, meltingPoint = 692.68, boilingPoint = 1180.0,
                    oxidationStates = "+2",
                    yearDiscovered = "1746", discoverer = "Andreas Sigismund Marggraf",
                    applications = "Zinc is used in galvanizing steel to prevent corrosion, in die-casting in the automobile industry, in batteries, and as an alloy in brass and bronze. It's essential for human immune function, wound healing, DNA synthesis, and cell division. It's found in minerals like sphalerite and smithsonite."),
            
            Element(31, "Ga", "Gallium", 4, 13, ElementCategory.POST_TRANSITION_METAL, 69.723, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p¹",
                    density = 5.91, meltingPoint = 29.76, boilingPoint = 2400.0,
                    oxidationStates = "+3, +2, +1",
                    yearDiscovered = "1875", discoverer = "Paul Émile Lecoq de Boisbaudran",
                    applications = "Gallium is used in semiconductors, particularly in gallium arsenide (GaAs) and gallium nitride (GaN) for LEDs, laser diodes, solar cells, and integrated circuits. It's also used in high-temperature thermometers and in some medical applications. It's notable for its very low melting point and can melt in your hand. It's found in trace amounts in zinc ores and bauxite."),
            Element(32, "Ge", "Germanium", 4, 14, ElementCategory.METALLOID, 72.630, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p²",
                    density = 5.32, meltingPoint = 1211.4, boilingPoint = 3106.0,
                    oxidationStates = "+4, +2",
                    yearDiscovered = "1886", discoverer = "Clemens Winkler",
                    applications = "Germanium is used in fiber-optic systems, infrared optics, solar cell applications, and as a semiconductor in electronics. It was used in the first transistors before silicon became dominant. It's found in small amounts in zinc ores, coal, and some copper ores. It's also present in certain rare minerals like germanite and argyrodite."),
            Element(33, "As", "Arsenic", 4, 15, ElementCategory.METALLOID, 74.922, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p³",
                    density = 5.72, meltingPoint = 1090.0, boilingPoint = 887.0,
                    oxidationStates = "+5, +3, -3",
                    yearDiscovered = "Ancient", discoverer = "Ancient civilizations",
                    applications = "Arsenic is used in wood preservatives, pesticides, herbicides, and in the semiconductor industry (gallium arsenide). Historically, it was used in pigments and medicinally, though it's highly toxic. It's found in minerals like arsenopyrite, realgar, and orpiment, and occurs naturally in some groundwater sources."),
            Element(34, "Se", "Selenium", 4, 16, ElementCategory.NONMETAL, 78.971, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁴",
                    density = 4.79, meltingPoint = 494.0, boilingPoint = 958.0,
                    oxidationStates = "+6, +4, +2, -2",
                    yearDiscovered = "1817", discoverer = "Jöns Jacob Berzelius",
                    applications = "Selenium is used in electronics for photocells, solar cells, and photocopiers. It's also used in glass production to decolorize glass, in pigments, and as a nutritional supplement for livestock and humans. It's an essential trace element in human nutrition. It's found in copper and sulfide ores, and some soils."),
            Element(35, "Br", "Bromine", 4, 17, ElementCategory.HALOGEN, 79.904, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁵",
                    density = 3.12, meltingPoint = 265.8, boilingPoint = 332.0,
                    oxidationStates = "+5, +1, -1",
                    yearDiscovered = "1826", discoverer = "Antoine Jérôme Balard",
                    applications = "Bromine is used in flame retardants, water purification, pesticides, pharmaceuticals, and as a drilling fluid in oil and gas wells. It's one of only two elements that are liquid at room temperature (the other being mercury). It naturally occurs in seawater, salt lakes, and underground brine deposits."),
            Element(36, "Kr", "Krypton", 4, 18, ElementCategory.NOBLE_GAS, 83.798, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶",
                    density = 3.75, meltingPoint = 115.79, boilingPoint = 119.93,
                    oxidationStates = "0",
                    yearDiscovered = "1898", discoverer = "William Ramsay & Morris Travers",
                    applications = "Krypton is used in certain types of photographic flashes, high-powered gas lasers, and was formerly used in some light bulbs. It's also been used in combination with other gases in fluorescent lamps. It occurs in trace amounts in the Earth's atmosphere (about 1 ppm)."),

            Element(37, "Rb", "Rubidium", 5, 1, ElementCategory.ALKALI_METAL, 85.4678, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s¹",
                density = 1.532, meltingPoint = 312.45, boilingPoint = 961.0,
                oxidationStates = "+1",
                yearDiscovered = "1861", discoverer = "Robert Bunsen & Gustav Kirchhoff",
                applications = "Rubidium is used in research, specialty glasses, and electronics. It's also considered for ion engines and atomic clocks. Rubidium compounds are used in fireworks and as a getter in vacuum tubes. It occurs naturally in minerals like lepidolite and pollucite."),

            Element(38, "Sr", "Strontium", 5, 2, ElementCategory.ALKALINE_EARTH_METAL, 87.62, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s²",
                density = 2.64, meltingPoint = 1050.0, boilingPoint = 1655.0,
                oxidationStates = "+2",
                yearDiscovered = "1790", discoverer = "Adair Crawford",
                applications = "Strontium is used in fireworks (red color), magnets, ceramic ferrites, and historically in cathode ray tubes. Strontium-90, a radioactive isotope, is used in cancer therapy and as a heat source in radioisotope thermoelectric generators. It's found in celestine and strontianite ores."),

            Element(39, "Y", "Yttrium", 5, 3, ElementCategory.TRANSITION_METAL, 88.90584, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹",
                density = 4.47, meltingPoint = 1799.0, boilingPoint = 3609.0,
                oxidationStates = "+3",
                yearDiscovered = "1794", discoverer = "Johan Gadolin",
                applications = "Yttrium is used in LEDs, phosphors (e.g., in CRTs and LEDs), superconductors, and medical applications such as cancer treatment. Yttrium-aluminum garnet (YAG) is used in lasers. It is often found in rare earth minerals like xenotime and monazite."),

            Element(40, "Zr", "Zirconium", 5, 4, ElementCategory.TRANSITION_METAL, 91.224, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d²",
                density = 6.52, meltingPoint = 2128.0, boilingPoint = 4682.0,
                oxidationStates = "+4",
                yearDiscovered = "1789", discoverer = "Martin Heinrich Klaproth",
                applications = "Zirconium is used in nuclear reactors due to its low neutron-capture cross-section, as well as in surgical instruments, catalytic converters, and ceramic materials. Zircon (ZrSiO₄) is its main ore and is also used as a gemstone."),

            Element(41, "Nb", "Niobium", 5, 5, ElementCategory.TRANSITION_METAL, 92.90637, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s¹ 4d⁴",
                density = 8.57, meltingPoint = 2750.0, boilingPoint = 5017.0,
                oxidationStates = "+5, +3",
                yearDiscovered = "1801", discoverer = "Charles Hatchett",
                applications = "Niobium is used in alloys, especially with steel to improve strength, superconducting materials, and in aerospace and pipeline construction. It's also used in superconducting magnets for MRI scanners. The main source is the mineral columbite."),

            Element(42, "Mo", "Molybdenum", 5, 6, ElementCategory.TRANSITION_METAL, 95.95, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s¹ 4d⁵",
                density = 10.28, meltingPoint = 2896.0, boilingPoint = 4912.0,
                oxidationStates = "+6, +5, +4, +3, +2",
                yearDiscovered = "1778", discoverer = "Carl Wilhelm Scheele",
                applications = "Molybdenum is used in steel alloys to increase hardness, strength, and resistance to corrosion. It’s also essential in enzymes in organisms. Commonly extracted from the ore molybdenite (MoS₂)."),

            Element(43, "Tc", "Technetium", 5, 7, ElementCategory.TRANSITION_METAL, 98.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d⁵",
                density = 11.5, meltingPoint = 2430.0, boilingPoint = 4538.0,
                oxidationStates = "+7, +5, +4",
                yearDiscovered = "1937", discoverer = "Carlo Perrier & Emilio Segrè",
                applications = "Technetium is primarily used in medical diagnostics, especially in radiopharmaceuticals for imaging (e.g., technetium-99m). It's also studied for corrosion resistance in steel. It's the lightest element with no stable isotopes and is typically produced synthetically."),

            Element(44, "Ru", "Ruthenium", 5, 8, ElementCategory.TRANSITION_METAL, 101.07, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s¹ 4d⁷",
                density = 12.37, meltingPoint = 2607.0, boilingPoint = 4423.0,
                oxidationStates = "+4, +3, +2, +1, -2",
                yearDiscovered = "1844", discoverer = "Karl Ernst Claus",
                applications = "Ruthenium is used in electronics, electrical contacts, and as a catalyst. It is also used in wear-resistant alloys and in some advanced solar cells. It's part of the platinum group and is found in ores with platinum and nickel."),

            Element(45, "Rh", "Rhodium", 5, 9, ElementCategory.TRANSITION_METAL, 102.9055, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s¹ 4d⁸",
                density = 12.41, meltingPoint = 2237.0, boilingPoint = 3968.0,
                oxidationStates = "+3, +1",
                yearDiscovered = "1803", discoverer = "William Hyde Wollaston",
                applications = "Rhodium is primarily used in catalytic converters to reduce vehicle emissions. It's also used in jewelry (for plating), mirrors, and electrical contacts. It's one of the rarest and most valuable precious metals."),

            Element(46, "Pd", "Palladium", 5, 10, ElementCategory.TRANSITION_METAL, 106.42, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s⁰ 4d¹⁰",
                density = 12.02, meltingPoint = 1828.05, boilingPoint = 3236.0,
                oxidationStates = "+2, +4, 0",
                yearDiscovered = "1803", discoverer = "William Hyde Wollaston",
                applications = "Palladium is used in catalytic converters, electronics, hydrogen purification, dentistry, and jewelry. It's also used in fuel cells and as a catalyst in chemical reactions. It is one of the platinum-group metals and occurs with nickel and copper ores."),

            Element(47, "Ag", "Silver", 5, 11, ElementCategory.TRANSITION_METAL, 107.87, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s¹ 4d¹⁰",
                    density = 10.49, meltingPoint = 1234.93, boilingPoint = 2435.0,
                    oxidationStates = "+1",
                    yearDiscovered = "3000 BC", discoverer = "Ancient civilizations",
                    applications = "Silver is used in jewelry, silverware, currency, photography (though diminishing with digital), electronics due to its excellent conductivity, mirrors, solar panels, and as an antimicrobial agent in medical applications. It has the highest electrical and thermal conductivity of any metal. It occurs naturally in minerals like argentite and is often found alongside lead, copper, gold, and zinc ores."),

            Element(48, "Cd", "Cadmium", 5, 12, ElementCategory.POST_TRANSITION_METAL, 112.414, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰",
                density = 8.65, meltingPoint = 594.22, boilingPoint = 1040.0,
                oxidationStates = "+2",
                yearDiscovered = "1817", discoverer = "Friedrich Stromeyer",
                applications = "Cadmium is used in rechargeable batteries (NiCd), pigments, coatings, and as a stabilizer in plastics. It's toxic and environmentally restricted. Cadmium is a byproduct of zinc refining."),

            Element(49, "In", "Indium", 5, 13, ElementCategory.POST_TRANSITION_METAL, 114.818, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p¹",
                density = 7.31, meltingPoint = 429.75, boilingPoint = 2345.0,
                oxidationStates = "+3, +1",
                yearDiscovered = "1863", discoverer = "Ferdinand Reich & Hieronymous Theodor Richter",
                applications = "Indium is used in touchscreens, flat-panel displays (as indium tin oxide), solders, and semiconductors. It's rare and usually recovered as a byproduct of zinc mining."),

            Element(50, "Sn", "Tin", 5, 14, ElementCategory.POST_TRANSITION_METAL, 118.710, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p²",
                density = 7.31, meltingPoint = 505.08, boilingPoint = 2875.0,
                oxidationStates = "+4, +2",
                yearDiscovered = "Ancient", discoverer = "Ancient civilizations",
                applications = "Tin is used in solder, plating for steel cans, and alloys like bronze. It's corrosion-resistant and has been used for millennia. It’s found in the ore cassiterite."),

            Element(51, "Sb", "Antimony", 5, 15, ElementCategory.METALLOID, 121.760, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p³",
                density = 6.697, meltingPoint = 903.78, boilingPoint = 1860.0,
                oxidationStates = "+5, +3, -3",
                yearDiscovered = "Ancient", discoverer = "Ancient civilizations",
                applications = "Antimony is used in flame retardants, lead-acid batteries, alloys, and semiconductors. It's found in stibnite (Sb₂S₃) and has been used since antiquity."),

            Element(52, "Te", "Tellurium", 5, 16, ElementCategory.METALLOID, 127.60, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁴",
                density = 6.24, meltingPoint = 722.66, boilingPoint = 1261.0,
                oxidationStates = "+6, +4, -2",
                yearDiscovered = "1782", discoverer = "Franz-Joseph Müller von Reichenstein",
                applications = "Tellurium is used in thermoelectrics, solar panels (CdTe), steel alloys, and semiconductors. It’s a rare metalloid found in copper and lead ores."),

            Element(53, "I", "Iodine", 5, 17, ElementCategory.HALOGEN, 126.90447, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁵",
                density = 4.93, meltingPoint = 386.85, boilingPoint = 457.4,
                oxidationStates = "+7, +5, +1, -1",
                yearDiscovered = "1811", discoverer = "Bernard Courtois",
                applications = "Iodine is used in medicine (antiseptics, contrast agents), nutrition (iodized salt), and dyes. It’s essential for thyroid hormones and is found in seawater and seaweed."),

            Element(54, "Xe", "Xenon", 5, 18, ElementCategory.NOBLE_GAS, 131.293, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶",
                density = 5.89, meltingPoint = 161.4, boilingPoint = 165.0,
                oxidationStates = "0, +2, +4, +6, +8",
                yearDiscovered = "1898", discoverer = "William Ramsay & Morris Travers",
                applications = "Xenon is used in high-intensity lamps, ion propulsion, anesthesia, and nuclear reactors. It's one of the few noble gases that forms compounds, like xenon hexafluoroplatinate."),

            Element(55, "Cs", "Cesium", 6, 1, ElementCategory.ALKALI_METAL, 132.90545, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s¹",
                density = 1.93, meltingPoint = 301.7, boilingPoint = 944.0,
                oxidationStates = "+1",
                yearDiscovered = "1860", discoverer = "Robert Bunsen & Gustav Kirchhoff",
                applications = "Cesium is used in atomic clocks, photoelectric cells, and drilling fluids. It's highly reactive with water and stored in mineral oil. Found in the mineral pollucite."),

            Element(56, "Ba", "Barium", 6, 2, ElementCategory.ALKALINE_EARTH_METAL, 137.327, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s²",
                density = 3.62, meltingPoint = 1000.0, boilingPoint = 2170.0,
                oxidationStates = "+2",
                yearDiscovered = "1808", discoverer = "Humphry Davy",
                applications = "Barium is used in drilling fluids, paints, ceramics, and medical imaging (barium meals). It's found in barite (BaSO₄) and witherite (BaCO₃)."),

            Element(57, "La", "Lanthanum", 6, 3, ElementCategory.LANTHANIDE, 138.90547, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 5d¹",
                density = 6.15, meltingPoint = 1193.0, boilingPoint = 3737.0,
                oxidationStates = "+3",
                yearDiscovered = "1839", discoverer = "Carl Gustaf Mosander",
                applications = "Lanthanum is used in optical lenses, batteries (nickel-metal hydride), and catalysts. It occurs in monazite and bastnasite ores."),

            Element(58, "Ce", "Cerium", 6, 3, ElementCategory.LANTHANIDE, 140.116,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹ 5d¹",
                density = 6.77, meltingPoint = 795.0, boilingPoint = 3443.0,
                oxidationStates = "+4, +3",
                yearDiscovered = "1803", discoverer = "Martin Heinrich Klaproth",
                applications = "Used in catalytic converters, glass polishing, and as a catalyst in self-cleaning ovens."),

            Element(59, "Pr", "Praseodymium", 6, 3, ElementCategory.LANTHANIDE, 140.90766,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f³",
                density = 6.77, meltingPoint = 931.0, boilingPoint = 3520.0,
                oxidationStates = "+3, +4",
                yearDiscovered = "1885", discoverer = "Carl Auer von Welsbach",
                applications = "Used in aircraft engines, studio lighting, and as a core material in carbon arc lights."),

            Element(60, "Nd", "Neodymium", 6, 3, ElementCategory.LANTHANIDE, 144.242,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f⁴",
                density = 7.01, meltingPoint = 1024.0, boilingPoint = 3074.0,
                oxidationStates = "+3",
                yearDiscovered = "1885", discoverer = "Carl Auer von Welsbach",
                applications = "Used in powerful magnets, lasers, and as a colorant in glasses and enamels."),

            Element(61, "Pm", "Promethium", 6, 3, ElementCategory.LANTHANIDE, 145.0,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f⁵",
                density = 7.26, meltingPoint = 1042.0, boilingPoint = 3000.0,
                oxidationStates = "+3",
                yearDiscovered = "1945", discoverer = "Jacob A. Marinsky, Lawrence E. Glendenin, Charles D. Coryell",
                applications = "Used in luminous paint, atomic batteries, and as a heat source for auxiliary power in space probes."),

            Element(62, "Sm", "Samarium", 6, 3, ElementCategory.LANTHANIDE, 150.36,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f⁶",
                density = 7.52, meltingPoint = 1072.0, boilingPoint = 1794.0,
                oxidationStates = "+3, +2",
                yearDiscovered = "1879", discoverer = "Lecoq de Boisbaudran",
                applications = "Used in magnets, lasers, and as a neutron absorber in nuclear reactors."),

            Element(63, "Eu", "Europium", 6, 3, ElementCategory.LANTHANIDE, 151.964,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f⁷",
                density = 5.24, meltingPoint = 826.0, boilingPoint = 1529.0,
                oxidationStates = "+3, +2",
                yearDiscovered = "1901", discoverer = "Eugène-Anatole Demarçay",
                applications = "Used in phosphorescent and fluorescent applications, such as in television screens and fluorescent lamps."),

            Element(64, "Gd", "Gadolinium", 6, 3, ElementCategory.LANTHANIDE, 157.25,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f⁷ 5d¹",
                density = 7.90, meltingPoint = 1312.0, boilingPoint = 3273.0,
                oxidationStates = "+3",
                yearDiscovered = "1880", discoverer = "Jean Charles Galissard de Marignac",
                applications = "Used in MRI contrast agents, neutron capture, and as a component in phosphors and magnets."),

            Element(65, "Tb", "Terbium", 6, 3, ElementCategory.LANTHANIDE, 158.92535,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f⁹",
                density = 8.23, meltingPoint = 1356.0, boilingPoint = 3230.0,
                oxidationStates = "+3, +4",
                yearDiscovered = "1843", discoverer = "Carl Gustaf Mosander",
                applications = "Used in solid-state devices, phosphors in lighting, and as a dopant in calcium fluoride, calcium tungstate, and strontium molybdate."),

            Element(66, "Dy", "Dysprosium", 6, 3, ElementCategory.LANTHANIDE, 162.500,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁰",
                density = 8.54, meltingPoint = 1407.0, boilingPoint = 2567.0,
                oxidationStates = "+3",
                yearDiscovered = "1886", discoverer = "Paul Émile Lecoq de Boisbaudran",
                applications = "Used in magnets, lasers, and as a neutron absorber in nuclear reactors."),

            Element(67, "Ho", "Holmium", 6, 3, ElementCategory.LANTHANIDE, 164.93033,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹¹",
                density = 8.79, meltingPoint = 1461.0, boilingPoint = 2720.0,
                oxidationStates = "+3",
                yearDiscovered = "1878", discoverer = "Marc Delafontaine and Jacques-Louis Soret",
                applications = "Used in nuclear reactors as a burnable poison, and in solid-state lasers and optical fibers."),

            Element(68, "Er", "Erbium", 6, 3, ElementCategory.LANTHANIDE, 167.259,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹²",
                density = 9.07, meltingPoint = 1529.0, boilingPoint = 2868.0,
                oxidationStates = "+3",
                yearDiscovered = "1843", discoverer = "Carl Gustaf Mosander",
                applications = "Used in optical fibers, lasers, and as a photographic filter."),

            Element(69, "Tm", "Thulium", 6, 3, ElementCategory.LANTHANIDE, 168.93422, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹³",
                density = 9.32, meltingPoint = 1818.0, boilingPoint = 2223.0,
                oxidationStates = "+3",
                yearDiscovered = "1879", discoverer = "Per Teodor Cleve",
                applications = "Thulium is used in portable X-ray machines, lasers, and solid-state devices. It’s the second rarest of the lanthanides and occurs in minerals like monazite and xenotime."),

            Element(70, "Yb", "Ytterbium", 6, 3, ElementCategory.LANTHANIDE, 173.045, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴",
                density = 6.90, meltingPoint = 1097.0, boilingPoint = 1469.0,
                oxidationStates = "+3, +2",
                yearDiscovered = "1878", discoverer = "Jean Charles Galissard de Marignac",
                applications = "Ytterbium is used in lasers, atomic clocks, and as a doping agent in fiber optics. It’s found in minerals like gadolinite, monazite, and xenotime."),

            Element(71, "Lu", "Lutetium", 6, 3, ElementCategory.LANTHANIDE, 174.9668, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹",
                density = 9.84, meltingPoint = 1936.0, boilingPoint = 3675.0,
                oxidationStates = "+3",
                yearDiscovered = "1907", discoverer = "Georges Urbain",
                applications = "Lutetium is used in PET scan detectors, catalysts, and cancer therapy. It’s the heaviest and one of the rarest lanthanides, found in minerals like monazite and xenotime."),

            Element(72, "Hf", "Hafnium", 6, 4, ElementCategory.TRANSITION_METAL, 178.49, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 4f¹⁴ 5d²",
                density = 13.31, meltingPoint = 2506.0, boilingPoint = 4876.0,
                oxidationStates = "+4",
                yearDiscovered = "1923", discoverer = "Dirk Coster & George de Hevesy",
                applications = "Hafnium is used in nuclear reactors (control rods), high-temperature alloys, and microprocessors. It’s found with zirconium in zircon ores."),

            Element(73, "Ta", "Tantalum", 6, 5, ElementCategory.TRANSITION_METAL, 180.94788, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 4f¹⁴ 5d³",
                density = 16.69, meltingPoint = 3290.0, boilingPoint = 5731.0,
                oxidationStates = "+5",
                yearDiscovered = "1802", discoverer = "Anders Ekeberg",
                applications = "Tantalum is used in capacitors, surgical implants, and high-temperature alloys. It’s corrosion-resistant and found in tantalite and coltan."),

            Element(74, "W", "Tungsten", 6, 6, ElementCategory.TRANSITION_METAL, 183.84, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 4f¹⁴ 5d⁴",
                density = 19.25, meltingPoint = 3695.0, boilingPoint = 5828.0,
                oxidationStates = "+6, +4, +2",
                yearDiscovered = "1783", discoverer = "José & Fausto Elhuyar",
                applications = "Tungsten is used in light bulb filaments, cutting tools, and armor-piercing ammunition. It has the highest melting point of all metals."),

            Element(75, "Re", "Rhenium", 6, 7, ElementCategory.TRANSITION_METAL, 186.207, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 4f¹⁴ 5d⁵",
                density = 21.02, meltingPoint = 3459.0, boilingPoint = 5869.0,
                oxidationStates = "+7, +6, +4",
                yearDiscovered = "1925", discoverer = "Walter & Ida Noddack, Otto Berg",
                applications = "Rhenium is used in superalloys for jet engines and catalysts (especially in reforming gasoline). It's one of the rarest elements in Earth's crust."),

            Element(76, "Os", "Osmium", 6, 8, ElementCategory.TRANSITION_METAL, 190.23, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 4f¹⁴ 5d⁶",
                density = 22.59, meltingPoint = 3306.0, boilingPoint = 5285.0,
                oxidationStates = "+8, +4",
                yearDiscovered = "1803", discoverer = "Smithson Tennant",
                applications = "Osmium is used in fountain pen tips, electrical contacts, and instrument pivots. It’s the densest naturally occurring element."),

            Element(77, "Ir", "Iridium", 6, 9, ElementCategory.TRANSITION_METAL, 192.217, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 4f¹⁴ 5d⁷",
                density = 22.56, meltingPoint = 2719.0, boilingPoint = 4701.0,
                oxidationStates = "+4, +3",
                yearDiscovered = "1803", discoverer = "Smithson Tennant",
                applications = "Iridium is used in spark plugs, crucibles, and deep-sea telecommunication. It’s extremely corrosion-resistant and rare."),

            Element(78, "Pt", "Platinum", 6, 10, ElementCategory.TRANSITION_METAL, 195.084, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s¹ 4f¹⁴ 5d⁹",
                density = 21.45, meltingPoint = 2041.4, boilingPoint = 4098.0,
                oxidationStates = "+4, +2",
                yearDiscovered = "1735", discoverer = "Antonio de Ulloa",
                applications = "Platinum is used in catalytic converters, jewelry, electrical contacts, and medical implants. It's highly valuable, inert, and part of the platinum group metals."),

            Element(79, "Au", "Gold", 6, 11, ElementCategory.TRANSITION_METAL, 196.967, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s¹ 4f¹⁴ 5d¹⁰",
                    density = 19.32, meltingPoint = 1337.33, boilingPoint = 3243.0,
                    oxidationStates = "+3, +1",
                    yearDiscovered = "6000 BC", discoverer = "Ancient civilizations",
                    applications = "Gold is primarily used in jewelry, as a store of value and investment, in electronics for connectors and switch contacts, in dentistry, gilding, and in aerospace applications. It's highly malleable, resistant to corrosion, and an excellent conductor of electricity. It typically occurs in its native form as nuggets or grains in rocks, veins, and alluvial deposits."),
            
            Element(80, "Hg", "Mercury", 6, 12, ElementCategory.TRANSITION_METAL, 200.59, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 4f¹⁴ 5d¹⁰",
                    density = 13.54, meltingPoint = 234.32, boilingPoint = 629.88,
                    oxidationStates = "+2, +1",
                    yearDiscovered = "Ancient", discoverer = "Ancient civilizations",
                    applications = "Mercury is used in scientific instruments like thermometers and barometers (though being phased out), electrical switches, some types of lighting, and in dental amalgams (increasingly restricted). It's the only metal that's liquid at standard conditions. It occurs naturally in the mineral cinnabar and is highly toxic."),

            Element(81, "Tl", "Thallium", 6, 13, ElementCategory.POST_TRANSITION_METAL, 204.38,
                "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p¹",
                density = 11.85, meltingPoint = 577.0, boilingPoint = 1746.0,
                oxidationStates = "+3, +1",
                yearDiscovered = "1861", discoverer = "William Crookes",
                applications = "Thallium is used in electronics, infrared optics, and historically in rodenticides and insecticides (now restricted due to toxicity). It’s extremely toxic and mostly found in trace amounts in sulfide ores."),

            Element(82, "Pb", "Lead", 6, 14, ElementCategory.POST_TRANSITION_METAL, 207.2, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p²",
                    density = 11.34, meltingPoint = 600.61, boilingPoint = 2022.0,
                    oxidationStates = "+4, +2",
                    yearDiscovered = "7000 BC", discoverer = "Ancient civilizations",
                    applications = "Lead is used in batteries, radiation shielding, sound insulation, and historically in plumbing, paints, and gasoline additives (now restricted in many countries due to its toxicity). It's very malleable, ductile, and resistant to corrosion. It occurs naturally in minerals like galena and is often found with zinc, silver, and copper."),

            Element(83, "Bi", "Bismuth", 6, 15, ElementCategory.POST_TRANSITION_METAL, 208.9804, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p³",
                density = 9.78, meltingPoint = 544.7, boilingPoint = 1837.0,
                oxidationStates = "+3, +5",
                yearDiscovered = "Ancient", discoverer = "Known since ancient times",
                applications = "Bismuth is used in cosmetics, pharmaceuticals (e.g., Pepto-Bismol), alloys, and low-melting-point metal applications. It's a nontoxic replacement for lead in various uses and is often found in lead ores."),

            Element(84, "Po", "Polonium", 6, 16, ElementCategory.POST_TRANSITION_METAL, 209.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁴",
                density = 9.20, meltingPoint = 527.0, boilingPoint = 1235.0,
                oxidationStates = "+2, +4",
                yearDiscovered = "1898", discoverer = "Marie Curie & Pierre Curie",
                applications = "Polonium is used as a heat source in space probes and in antistatic devices. It's highly radioactive and toxic, found in uranium ores in trace amounts."),

            Element(85, "At", "Astatine", 6, 17, ElementCategory.HALOGEN, 210.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁵",
                density = 7.0, meltingPoint = 575.0, boilingPoint = 610.0,
                oxidationStates = "-1, +1, +3, +5, +7",
                yearDiscovered = "1940", discoverer = "Dale R. Corson, Kenneth Ross MacKenzie, Emilio Segrè",
                applications = "Astatine is used in limited medical research, particularly in targeted alpha-particle cancer therapy. It's extremely rare and radioactive, typically produced artificially."),

            Element(86, "Rn", "Radon", 6, 18, ElementCategory.NOBLE_GAS, 222.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶",
                density = 9.73, meltingPoint = 202.0, boilingPoint = 211.3,
                oxidationStates = "0, +2",
                yearDiscovered = "1900", discoverer = "Friedrich Ernst Dorn",
                applications = "Radon is used in cancer radiotherapy and geological research. It's a radioactive gas produced from uranium decay and poses health risks due to inhalation in confined areas."),

            Element(87, "Fr", "Francium", 7, 1, ElementCategory.ALKALI_METAL, 223.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s¹",
                density = 1.87, meltingPoint = 300.0, boilingPoint = 950.0,
                oxidationStates = "+1",
                yearDiscovered = "1939", discoverer = "Marguerite Perey",
                applications = "Francium has no commercial applications due to its extreme rarity and radioactivity. It's mainly used in scientific research and is produced synthetically in minute quantities."),

            Element(88, "Ra", "Radium", 7, 2, ElementCategory.ALKALINE_EARTH_METAL, 226.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s²",
                density = 5.5, meltingPoint = 973.0, boilingPoint = 1413.0,
                oxidationStates = "+2",
                yearDiscovered = "1898", discoverer = "Marie Curie & Pierre Curie",
                applications = "Radium was historically used in luminous paints and cancer treatments, though it's now largely replaced due to its radioactivity. It occurs in uranium ores."),

            Element(89, "Ac", "Actinium", 7, 3, ElementCategory.ACTINIDE, 227.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 6d¹",
                density = 10.07, meltingPoint = 1500.0, boilingPoint = 3500.0,
                oxidationStates = "+3",
                yearDiscovered = "1899", discoverer = "André-Louis Debierne",
                applications = "Actinium is used as a neutron source and in cancer radiotherapy research. It's radioactive and found in trace amounts in uranium ores."),

            Element(90, "Th", "Thorium", 7, 3, ElementCategory.ACTINIDE, 232.0377, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 6d²",
                density = 11.72, meltingPoint = 2023.0, boilingPoint = 5061.0,
                oxidationStates = "+4",
                yearDiscovered = "1829", discoverer = "Jöns Jacob Berzelius",
                applications = "Thorium is used in nuclear reactors, high-quality lenses, and some metal alloys. It's mildly radioactive and occurs in monazite sands."),

            Element(91, "Pa", "Protactinium", 7, 3, ElementCategory.ACTINIDE, 231.03588, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f² 6d¹",
                density = 15.37, meltingPoint = 1841.0, boilingPoint = 4300.0,
                oxidationStates = "+5, +4",
                yearDiscovered = "1913", discoverer = "Kasimir Fajans & Oswald Helmuth Göhring",
                applications = "Protactinium is mainly used in scientific research due to its scarcity and high radioactivity. It’s found in uranium ores in minute amounts."),

            Element(92, "U", "Uranium", 7, 6, ElementCategory.ACTINIDE, 238.03, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f³ 6d¹",
                    density = 19.1, meltingPoint = 1405.3, boilingPoint = 4404.0,
                    oxidationStates = "+6, +4, +3",
                    yearDiscovered = "1789", discoverer = "Martin Heinrich Klaproth",
                    applications = "Uranium is primarily used as fuel in nuclear power plants and in nuclear weapons. It's also used in some high-density armor penetrating ammunition, radiation shielding, and as counterweights in aircraft. It occurs naturally in minerals like uraninite (pitchblende) and carnotite at about 3 parts per million in Earth's crust."),

            Element(93, "Np", "Neptunium", 7, 3, ElementCategory.ACTINIDE, 237.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f⁴ 6d¹",
                density = 20.45, meltingPoint = 917.0, boilingPoint = 4000.0,
                oxidationStates = "+5, +4, +3, +6",
                yearDiscovered = "1940", discoverer = "Edwin McMillan & Philip Abelson",
                applications = "Neptunium is used in nuclear research and as a precursor for plutonium-238 production. It's radioactive and found in spent nuclear fuel."),

            Element(94, "Pu", "Plutonium", 9, 8, ElementCategory.ACTINIDE, 244.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 4s² 3d¹⁰ 4p⁶ 5s² 4d¹⁰ 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f⁶",
                    density = 19.84, meltingPoint = 912.5, boilingPoint = 3505.0,
                    oxidationStates = "+6, +5, +4, +3",
                    yearDiscovered = "1940", discoverer = "Glenn T. Seaborg, Arthur C. Wahl, Joseph W. Kennedy, Edwin M. McMillan",
                    applications = "Plutonium is primarily used in nuclear weapons and as fuel in some nuclear reactors. It's also used in radioisotope thermoelectric generators for space probes. It's not found naturally in significant amounts on Earth and is primarily produced in nuclear reactors through uranium-238 neutron capture."),

            Element(95, "Am", "Americium", 7, 3, ElementCategory.ACTINIDE, 243.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f⁷",
                density = 13.67, meltingPoint = 1449.0, boilingPoint = 2880.0,
                oxidationStates = "+3, +4, +5, +6",
                yearDiscovered = "1944", discoverer = "Glenn T. Seaborg et al.",
                applications = "Americium is used in smoke detectors and neutron sources. It’s synthetic and radioactive."),

            Element(96, "Cm", "Curium", 7, 3, ElementCategory.ACTINIDE, 247.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f⁷ 6d¹",
                density = 13.51, meltingPoint = 1613.0, boilingPoint = 3383.0,
                oxidationStates = "+3, +4",
                yearDiscovered = "1944", discoverer = "Albert Ghiorso, Glenn T. Seaborg, Ralph A. James",
                applications = "Curium is used in space probe power sources and as a neutron source. It’s highly radioactive and synthetic."),

            Element(97, "Bk", "Berkelium", 7, 3, ElementCategory.ACTINIDE, 247.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f⁹",
                density = 14.78, meltingPoint = 1259.0, boilingPoint = 2900.0,
                oxidationStates = "+3, +4",
                yearDiscovered = "1949", discoverer = "Lawrence Berkeley National Laboratory",
                applications = "Berkelium is used in scientific research, particularly to synthesize heavier elements like tennessine."),

            Element(98, "Cf", "Californium", 7, 3, ElementCategory.ACTINIDE, 251.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f¹⁰",
                density = 15.10, meltingPoint = 1173.0, boilingPoint = 1743.0,
                oxidationStates = "+2, +3, +4",
                yearDiscovered = "1950", discoverer = "Lawrence Berkeley National Laboratory",
                applications = "Californium is used in neutron radiography and as a neutron source in mining and oil exploration."),

            Element(99, "Es", "Einsteinium", 7, 3, ElementCategory.ACTINIDE, 252.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f¹¹",
                density = 8.84, meltingPoint = 1133.0, boilingPoint = Double.NaN,
                oxidationStates = "+3",
                yearDiscovered = "1952", discoverer = "Albert Ghiorso et al.",
                applications = "Einsteinium is used only in scientific research. It was first identified in the debris of hydrogen bomb tests."),

            Element(100, "Fm", "Fermium", 7, 3, ElementCategory.ACTINIDE, 257.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f¹²",
                density = Double.NaN, meltingPoint = 1800.0, boilingPoint = Double.NaN,
                oxidationStates = "+3",
                yearDiscovered = "1952", discoverer = "Albert Ghiorso et al.",
                applications = "Fermium is used solely in scientific studies due to its short half-life and high radioactivity."),

            Element(101, "Md", "Mendelevium", 7, 3, ElementCategory.ACTINIDE, 258.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f¹³",
                density = Double.NaN, meltingPoint = 1100.0, boilingPoint = Double.NaN,
                oxidationStates = "+3, +2",
                yearDiscovered = "1955", discoverer = "Albert Ghiorso et al.",
                applications = "Mendelevium is only produced in tiny amounts for scientific research."),

            Element(102, "No", "Nobelium", 7, 3, ElementCategory.ACTINIDE, 259.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f¹⁴",
                density = Double.NaN, meltingPoint = 1100.0, boilingPoint = Double.NaN,
                oxidationStates = "+3, +2",
                yearDiscovered = "1958", discoverer = "Joint Institute for Nuclear Research",
                applications = "Nobelium is used only for basic scientific research."),

            Element(103, "Lr", "Lawrencium", 7, 3, ElementCategory.ACTINIDE, 266.0, "1s² 2s² 2p⁶ 3s² 3p⁶ 3d¹⁰ 4s² 4p⁶ 4d¹⁰ 5s² 5p⁶ 6s² 4f¹⁴ 5d¹⁰ 6p⁶ 7s² 5f¹⁴ 7p¹",
                density = Double.NaN, meltingPoint = 1900.0, boilingPoint = Double.NaN,
                oxidationStates = "+3",
                yearDiscovered = "1961", discoverer = "Albert Ghiorso et al.",
                applications = "Lawrencium is of interest for scientific research into heavy element behavior."),

            Element(104, "Rf", "Rutherfordium", 7, 4, ElementCategory.TRANSITION_METAL, 267.0, "1s² ... 7s² 5f¹⁴ 6d²",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+4",
                yearDiscovered = "1969", discoverer = "Joint Institute for Nuclear Research",
                applications = "Rutherfordium is a synthetic element with no known practical uses beyond scientific research."),

            Element(105, "Db", "Dubnium", 7, 5, ElementCategory.TRANSITION_METAL, 268.0, "1s² ... 7s² 5f¹⁴ 6d³",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+5",
                yearDiscovered = "1967", discoverer = "Joint Institute for Nuclear Research",
                applications = "Dubnium is used only for atomic research due to its short half-life."),

            Element(106, "Sg", "Seaborgium", 7, 6, ElementCategory.TRANSITION_METAL, 271.0, "1s² ... 7s² 5f¹⁴ 6d⁴",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+6",
                yearDiscovered = "1974", discoverer = "Lawrence Berkeley National Laboratory",
                applications = "Seaborgium has no practical uses outside of research."),

            Element(107, "Bh", "Bohrium", 7, 7, ElementCategory.TRANSITION_METAL, 270.0, "1s² ... 7s² 5f¹⁴ 6d⁵",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+7",
                yearDiscovered = "1981", discoverer = "GSI Helmholtz Centre for Heavy Ion Research",
                applications = "Bohrium is purely used in scientific experiments."),

            Element(108, "Hs", "Hassium", 7, 8, ElementCategory.TRANSITION_METAL, 277.0, "1s² ... 7s² 5f¹⁴ 6d⁶",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+8",
                yearDiscovered = "1984", discoverer = "GSI Helmholtz Centre for Heavy Ion Research",
                applications = "Hassium is under study for theoretical chemistry research only."),

            Element(109, "Mt", "Meitnerium", 7, 9, ElementCategory.TRANSITION_METAL, 278.0, "1s² ... 7s² 5f¹⁴ 6d⁷",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+1, +3",
                yearDiscovered = "1982", discoverer = "GSI Helmholtz Centre for Heavy Ion Research",
                applications = "Meitnerium is studied for nuclear properties only."),

            Element(110, "Ds", "Darmstadtium", 7, 10, ElementCategory.TRANSITION_METAL, 281.0, "1s² ... 7s² 5f¹⁴ 6d⁸",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+6",
                yearDiscovered = "1994", discoverer = "GSI Helmholtz Centre for Heavy Ion Research",
                applications = "Darmstadtium has no known practical uses."),

            Element(111, "Rg", "Roentgenium", 7, 11, ElementCategory.TRANSITION_METAL, 282.0, "1s² ... 7s² 5f¹⁴ 6d⁹",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+1, +3",
                yearDiscovered = "1994", discoverer = "GSI Helmholtz Centre for Heavy Ion Research",
                applications = "Roentgenium is used only in atomic structure studies."),

            Element(112, "Cn", "Copernicium", 7, 12, ElementCategory.POST_TRANSITION_METAL, 285.0, "1s² ... 7s² 5f¹⁴ 6d¹⁰",
                density = 14.0, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+2, +4",
                yearDiscovered = "1996", discoverer = "GSI Helmholtz Centre for Heavy Ion Research",
                applications = "Copernicium is still under study; potentially a volatile metal."),

            Element(113, "Nh", "Nihonium", 7, 13, ElementCategory.POST_TRANSITION_METAL, 286.0, "1s² ... 7s² 5f¹⁴ 6d¹⁰ 7p¹",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+1, +3",
                yearDiscovered = "2004", discoverer = "RIKEN, Japan",
                applications = "Nihonium has no applications beyond research."),

            Element(114, "Fl", "Flerovium", 7, 14, ElementCategory.POST_TRANSITION_METAL, 289.0, "1s² ... 7s² 5f¹⁴ 6d¹⁰ 7p²",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+2, +4",
                yearDiscovered = "1998", discoverer = "Joint Institute for Nuclear Research, Dubna",
                applications = "Flerovium is a subject of theoretical and experimental interest."),

            Element(115, "Mc", "Moscovium", 7, 15, ElementCategory.POST_TRANSITION_METAL, 290.0, "1s² ... 7s² 5f¹⁴ 6d¹⁰ 7p³",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+1, +3",
                yearDiscovered = "2003", discoverer = "Joint Institute for Nuclear Research and Lawrence Livermore",
                applications = "Moscovium is used only in heavy element research."),

            Element(116, "Lv", "Livermorium", 7, 16, ElementCategory.POST_TRANSITION_METAL, 293.0, "1s² ... 7s² 5f¹⁴ 6d¹⁰ 7p⁴",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "+2",
                yearDiscovered = "2000", discoverer = "Joint Institute for Nuclear Research and Lawrence Livermore",
                applications = "Livermorium is still being studied; its properties are largely unknown."),

            Element(117, "Ts", "Tennessine", 7, 17, ElementCategory.HALOGEN, 294.0, "1s² ... 7s² 5f¹⁴ 6d¹⁰ 7p⁵",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "-1, +1, +3, +5",
                yearDiscovered = "2010", discoverer = "Joint Institute for Nuclear Research and Oak Ridge",
                applications = "Tennessine is used in theoretical chemistry and element behavior modeling."),

            Element(118, "Og", "Oganesson", 7, 18, ElementCategory.NOBLE_GAS, 294.0, "1s² ... 7s² 5f¹⁴ 6d¹⁰ 7p⁶",
                density = Double.NaN, meltingPoint = Double.NaN, boilingPoint = Double.NaN,
                oxidationStates = "0, +2",
                yearDiscovered = "2002", discoverer = "Joint Institute for Nuclear Research and Lawrence Livermore",
                applications = "Oganesson is a subject of theoretical interest and may not be a typical noble gas."),

        )
        

    }
} 