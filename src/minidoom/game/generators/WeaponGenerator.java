package minidoom.game.generators;

import minidoom.game.GameEngine;
import minidoom.game.weapons.Weapon;

import java.util.EnumMap;

/**
 * Weapon generator is used to generate a weapon to be equipped by
 * the player. It is not an entity, as it is held directly by the player.
 */
public class WeaponGenerator {
	private EnumMap<Weapon.WeaponType, String> weapons;
	private GameEngine gameEngine;

	public WeaponGenerator(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
		weapons = new EnumMap<>(Weapon.WeaponType.class);
		weapons.put(Weapon.WeaponType.MELEE, "Melee");
		weapons.put(Weapon.WeaponType.GUN, "Gun");
		weapons.put(Weapon.WeaponType.MACHINEGUN, "MachineGun");
		weapons.put(Weapon.WeaponType.SHOTGUN, "Shotgun");
		weapons.put(Weapon.WeaponType.PLASMAGUN, "PlasmaGun");
		weapons.put(Weapon.WeaponType.ROCKETLAUNCHER, "RocketLauncher");
	}

	public Weapon generateWeapon(Weapon.WeaponType weaponType) {
		Weapon newWeapon = null;
		try {
			newWeapon = (Weapon)(Class.forName("minidoom.game.weapons." + weapons.get(weaponType)))
					.getDeclaredConstructor(gameEngine.getClass()).newInstance(gameEngine);
		} catch (Exception e) {
			System.out.println("Could not generate weapon: " +  weaponType.toString() + " : " + e.getMessage());
		}
		return newWeapon;
	}
}
