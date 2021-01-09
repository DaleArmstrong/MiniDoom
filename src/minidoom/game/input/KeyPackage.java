package minidoom.game.input;

/**
 * This is a package that gets created to pass to Control and assign
 * all the keys. This can be used by creating a custom keypackage and sending
 * it to control.
 */
public class KeyPackage {
	public int moveUp;
	public int moveDown;
	public int moveLeft;
	public int moveRight;
	public int fire;
	public int altFire;
	public int dodge;
	public int use;
	public int reload;
	public int swapMelee;
	public int swapGun;
	public int swapShotgun;
	public int swapMachinegun;
	public int swapRocketLauncher;
	public int swapPlasmagun;

	public KeyPackage(int moveUp, int moveDown, int moveLeft, int moveRight, int fire, int altFire, int dodge, int use,
	                  int reload, int swapMelee, int swapGun, int swapShotgun, int swapMachinegun,
	                  int swapRocketLauncher, int swapPlasmagun) {
		this.moveUp = moveUp;
		this.moveDown = moveDown;
		this.moveLeft = moveLeft;
		this.moveRight = moveRight;
		this.fire = fire;
		this.altFire = altFire;
		this.dodge = dodge;
		this.use = use;
		this.reload = reload;
		this.swapMelee = swapMelee;
		this.swapGun = swapGun;
		this.swapShotgun = swapShotgun;
		this.swapMachinegun = swapMachinegun;
		this.swapRocketLauncher = swapRocketLauncher;
		this.swapPlasmagun = swapPlasmagun;
	}
}
