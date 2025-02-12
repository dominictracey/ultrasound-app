package com.ultrasound.app.service;

import com.ultrasound.app.exceptions.ClassificationNotFoundException;
import com.ultrasound.app.exceptions.SubMenuNotFoundException;
import com.ultrasound.app.model.data.Classification;
import com.ultrasound.app.model.data.ListItem;
import com.ultrasound.app.model.data.SubMenu;
import com.ultrasound.app.repo.SubMenuRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
@Service
public class SubMenuServiceImpl implements SubMenuService{

    @Autowired
    private SubMenuRepo subMenuRepo;
    @Autowired
    private ClassificationServiceImpl classificationService;
    @Autowired
    private ItemServiceImpl itemService;

    @Override
    public SubMenu save(SubMenu subMenu) {
        return subMenuRepo.save(subMenu);
    }

    @Override
    public SubMenu getById(String id) {
        return subMenuRepo.findById(id).orElseThrow(() -> new SubMenuNotFoundException(id));
    }

    @Override
    public String insert(SubMenu subMenu) {
        return subMenuRepo.insert(subMenu).get_id();
    }

    @Override
    public String deleteById(String id) {
        SubMenu subMenu = getById(id);
        String name = subMenu.getName();
        int count = subMenu.getItemList().size();
        log.info("Deleting Submenu {} and {} listItems",name, count);
        subMenuRepo.delete(subMenu);
        return "Deleted submenu " + name + " and " + count + " list items";
    }

    @Override
    public String deleteByIdClassification(String classificationId, String subMenuId) {
        String subName = getById(subMenuId).getName();
        classificationService.deleteSubMenu(classificationId, subMenuId);
        deleteById(subMenuId);
        return "Deleted " + subName;
    }

    @Override
    public String editName(Classification classification, SubMenu subMenu, String id, String name) {
        String origName = subMenu.getName();
        Map<String, String> subMenus = classification.getSubMenus();
        subMenus.put(name, id);
        subMenus.remove(origName);
        classification.setSubMenus(subMenus);
        classificationService.save(classification);

        StringBuilder stringBuilder = new StringBuilder();
        subMenu.setName(name);
        subMenu.getItemList().forEach(listItem -> {
            stringBuilder.setLength(0);
            stringBuilder.append(classification.getName()).append(" ").append(name).append(" ").append(listItem.getName());
            listItem.setTitle(stringBuilder.toString());
        });
        stringBuilder.setLength(0);
        log.info("Changing Submenu name {} to {} in Classification: {}",origName, name, classification.getName());
        subMenuRepo.save(subMenu);
        stringBuilder.append("Changed Submenu name ").append(origName).append(" to ").append(name);
        return stringBuilder.toString();
    }

    @Override
    public String editItemName(String id, String currentName, String name, String link) {
        SubMenu subMenu = getById(id);
        String subName = subMenu.getName();
        List<ListItem> listItems = subMenu.getItemList();
        List<ListItem> itemList;

        ListItem item = itemService.findByLink(listItems, link, name, "submenu", subName);
        if (listItems.size() > 1) {
            itemList = itemService.removeItemFromList(listItems, link);
        } else {
            itemList = new ArrayList<>();
            itemList.add(item);
        }
        item.setName(name);
        itemList.add(item);
        subMenu.setItemList(itemList);
        save(subMenu);
        return "Saved " + currentName + " as " + name + " in " + subName;
    }

    @Override
    public void deleteTableEntities() {
        subMenuRepo.deleteAll();
    }

    @Override
    public Boolean isItemPresent(String id, String link) {
        SubMenu subMenu = subMenuRepo.findById(id).orElseThrow(
                () -> new ClassificationNotFoundException(id));
        List<ListItem> itemList = subMenu.getItemList();
        Predicate<ListItem> linkMatch = ListItem -> ListItem.getLink().equals(link);

        return itemList.stream().anyMatch(linkMatch);
    }


}
