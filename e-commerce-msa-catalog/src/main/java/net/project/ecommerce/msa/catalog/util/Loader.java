package net.project.ecommerce.msa.catalog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import net.project.ecommerce.msa.catalog.dao.ICategoryDAO;
import net.project.ecommerce.msa.catalog.dao.ICharacteristicDAO;
import net.project.ecommerce.msa.catalog.dao.IProductDAO;
import net.project.ecommerce.msa.catalog.dao.IProviderDAO;
import net.project.ecommerce.msa.catalog.dao.IRegionDAO;
import net.project.ecommerce.msa.catalog.dao.IWarehouseDAO;
import net.project.ecommerce.msa.catalog.model.AdmiCategory;
import net.project.ecommerce.msa.catalog.model.AdmiCharacteristic;
import net.project.ecommerce.msa.catalog.model.AdmiProvider;
import net.project.ecommerce.msa.catalog.model.AdmiRegion;
import net.project.ecommerce.msa.catalog.model.AdmiWarehouse;

@Component
public class Loader implements CommandLineRunner {
	
	@Autowired
	private IRegionDAO regionDAO;
	
	@Autowired
	private IWarehouseDAO warehouseDAO;
	
	@Autowired
	private IProviderDAO providerDAO;
	
	@Autowired
	private ICategoryDAO categoryDAO;
	
	@Autowired
	private ICharacteristicDAO characteristicDAO;
	
	@Autowired
	private IProductDAO productDAO;
	
	@Value("${catalog.product.loader:false}")
	private boolean isProductLoader;
		
	@Override
	public void run(String... args) throws Exception 
	{		
		
		if(isProductLoader)
		{
			//America
			AdmiRegion region = new AdmiRegion();		
			region.setParent("Americas");
			region.setName("North America - North");
			regionDAO.save(region);
			
			AdmiWarehouse warehouse = new AdmiWarehouse();
			warehouse.setName("Warehouse North");
			warehouse.setAddress("North");
			warehouse.setRegion(region);
			warehouse.setDeliveryTime("5");
			warehouseDAO.save(warehouse);
			
			region = new AdmiRegion();
			region.setParent("Americas");
			region.setName("North America - South");
			regionDAO.save(region);
			
			warehouse = new AdmiWarehouse();
			warehouse.setName("Warehouse South");
			warehouse.setAddress("South");
			warehouse.setRegion(region);
			warehouse.setDeliveryTime("5");
			warehouseDAO.save(warehouse);
			
			region = new AdmiRegion();
			region.setParent("Americas");
			region.setName("North America - West");
			regionDAO.save(region);
			
			warehouse = new AdmiWarehouse();
			warehouse.setName("Warehouse West");
			warehouse.setAddress("West");
			warehouse.setRegion(region);
			warehouse.setDeliveryTime("5");
			warehouseDAO.save(warehouse);
			
			region = new AdmiRegion();
			region.setParent("Americas");
			region.setName("North America - East");		
			regionDAO.save(region);		
			
			warehouse = new AdmiWarehouse();
			warehouse.setName("Warehouse East");
			warehouse.setAddress("East");
			warehouse.setRegion(region);
			warehouse.setDeliveryTime("5");
			warehouseDAO.save(warehouse);
			
			region = new AdmiRegion();
			region.setParent("Americas");
			region.setName("South America - North");
			regionDAO.save(region);
			region = new AdmiRegion();
			region.setParent("Americas");
			region.setName("South America - South");
			regionDAO.save(region);
			region = new AdmiRegion();
			region.setParent("Americas");
			region.setName("Central America - North");	
			regionDAO.save(region);
			region = new AdmiRegion();
			region.setParent("Americas");
			region.setName("Central America - South");	
			regionDAO.save(region);		
			//Europe
			region = new AdmiRegion();
			region.setParent("Europe");
			region.setName("Europe North");
			regionDAO.save(region);		
			region = new AdmiRegion();
			region.setParent("Europe");
			region.setName("Europe South");
			regionDAO.save(region);
			region = new AdmiRegion();
			region.setParent("Europe");
			region.setName("Europe West");
			regionDAO.save(region);
			region = new AdmiRegion();
			region.setParent("Europe");
			region.setName("Europe East");
			regionDAO.save(region);
			//Africa
			region = new AdmiRegion();
			region.setParent("Africa");
			region.setName("Africa North");
			regionDAO.save(region);		
			region = new AdmiRegion();
			region.setParent("Africa");
			region.setName("Africa South");
			regionDAO.save(region);
			region = new AdmiRegion();
			region.setParent("Africa");
			region.setName("Africa West");
			regionDAO.save(region);
			region = new AdmiRegion();
			region.setParent("Africa");
			region.setName("Africa East");
			regionDAO.save(region);
			//Oceania
			region = new AdmiRegion();
			region.setParent("Oceania");
			region.setName("Oceania North");
			regionDAO.save(region);		
			region = new AdmiRegion();
			region.setParent("Oceania");
			region.setName("Oceania South");
			regionDAO.save(region);
			//Asia
			region = new AdmiRegion();
			region.setParent("Asia");
			region.setName("Asia North");
			regionDAO.save(region);		
			region = new AdmiRegion();
			region.setParent("Asia");
			region.setName("Asia South");
			regionDAO.save(region);		
			
			AdmiProvider provider = new AdmiProvider();
			provider.setName("Provider A");
			provider.setProviderCode("COD-PROV-A");
			providerDAO.save(provider);
			
			AdmiCategory category = new AdmiCategory();
			category.setName("House");
			category.setProvider(provider);
			category.setCreatedBy("admin");
			categoryDAO.save(category);
			
			AdmiCategory childCategory = new AdmiCategory();
			childCategory.setCategory(category);
			childCategory.setName("Bedroom");
			childCategory.setProvider(provider);
			childCategory.setCreatedBy("admin");
			categoryDAO.save(childCategory);
			
			childCategory = new AdmiCategory();
			childCategory.setCategory(category);
			childCategory.setName("Livingroom");
			childCategory.setProvider(provider);
			childCategory.setCreatedBy("admin");
			categoryDAO.save(childCategory);
			
			childCategory = new AdmiCategory();
			childCategory.setCategory(category);
			childCategory.setName("Kitchen");
			childCategory.setProvider(provider);
			childCategory.setCreatedBy("admin");
			categoryDAO.save(childCategory);
			
			provider = new AdmiProvider();
			provider.setName("Provider B");
			provider.setProviderCode("COD-PROV-B");
			providerDAO.save(provider);
			
			category = new AdmiCategory();
			category.setName("House");
			category.setProvider(provider);
			category.setCreatedBy("admin");
			categoryDAO.save(category);
			
			childCategory = new AdmiCategory();
			childCategory.setCategory(category);
			childCategory.setName("Livingroom");
			childCategory.setProvider(provider);
			childCategory.setCreatedBy("admin");
			categoryDAO.save(childCategory);
			
			AdmiCharacteristic characteristic = new AdmiCharacteristic();
			characteristic.setName("Size");
			characteristicDAO.save(characteristic);
			characteristic = new AdmiCharacteristic();
			characteristic.setName("Colour");
			characteristicDAO.save(characteristic);
			characteristic = new AdmiCharacteristic();
			characteristic.setName("Weigth");
			characteristicDAO.save(characteristic);
			characteristic = new AdmiCharacteristic();
			characteristic.setName("Model");
			characteristicDAO.save(characteristic);
			characteristic = new AdmiCharacteristic();
			characteristic.setName("Brand");
			characteristicDAO.save(characteristic);
			
			//Gin index
			productDAO.createSearchVectorIndex();
		}
		
	}

}
