package Game.Sense.client.UI.NursultanGui;

import Game.Sense.client.UI.UwU.ImageButton;
import Game.Sense.client.module.feature.RENDER.ClickGUI;
import Game.Sense.client.UI.Minecraft.particle.ParticleUtils;
import Game.Sense.client.Helper.Utility.math.animations.Animation;
import Game.Sense.client.Helper.Utility.math.animations.impl.DecelerateAnimation;
import Game.Sense.client.Helper.Utility.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import Game.Sense.client.module.feature.ModuleCategory;
import Game.Sense.client.UI.NursultanGui.component.Component;
import Game.Sense.client.UI.NursultanGui.component.ExpandableComponent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends GuiScreen {
    public static boolean escapeKeyInUse;
    public float scale = 2;

    public List<Game.Sense.client.UI.NursultanGui.Panel> components = new ArrayList<>();
    public ScreenHelper screenHelper;
    public boolean exit = false;
    public ModuleCategory type;
    protected ArrayList<ImageButton> imageButtons = new ArrayList();
    private Component selectedPanel;
    private Animation initAnimation;
    public static GuiScreen oldScreen;
    private static ResourceLocation ANIME_GIRL;
    public static GuiSearcher search;

    public ClickGuiScreen() {
        int x = 20;
        int y = 80;
        for (ModuleCategory type : ModuleCategory.values()) {
            this.type = type;
            this.components.add(new Game.Sense.client.UI.NursultanGui.Panel(type, x, y));
            selectedPanel = new Game.Sense.client.UI.NursultanGui.Panel(type, x, y);
            x += width + 125;
        }

        this.screenHelper = new ScreenHelper(0, 0);
        oldScreen = this;
    }

    @Override
    public void initGui() {

        ScaledResolution sr = new ScaledResolution(mc);
        initAnimation = new DecelerateAnimation(600, 1);
        this.screenHelper = new ScreenHelper(0, 0);
        this.imageButtons.clear();

        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/config.png"), sr.getScaledWidth() - this.mc.robotoRegularFontRender.getStringWidth("Wellcome") - 89, sr.getScaledHeight() - this.mc.robotoRegularFontRender.getFontHeight() - 55, 50, 50, "", 22));
        search = new GuiSearcher(1337, this.mc.fontRendererObj, sr.getScaledWidth() / 2 + 320, 8080, 150, 18);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);




        if (!ClickGUI.potato_mode.getBoolValue() && ClickGUI.blur.getBoolValue() && ClickGUI.blurInt.getNumberValue() > 0) {
            if (this.mc.gameSettings.ofFastRender) {
                this.mc.gameSettings.ofFastRender = false;
            }
            RenderUtils.renderBlur(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), (int) ClickGUI.blurInt.getNumberValue());
        }
        if (!ClickGUI.potato_mode.getBoolValue() && ClickGUI.particles.getBoolValue()) {
            ParticleUtils.drawParticles(Mouse.getX() * this.width / this.mc.displayWidth, this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1);
        }
        if (ClickGUI.girl.getBoolValue()) {
            String animeGirlStr = "";
            if (ClickGUI.girlmode.currentMode.equals("Girl")) {
                animeGirlStr = "girl1";
            } else if (ClickGUI.girlmode.currentMode.equals("Rem")) {
                animeGirlStr = "girl2";
            } else if (ClickGUI.girlmode.currentMode.equals("Gachi")) {
                animeGirlStr = "girl3";
            } else if (ClickGUI.girlmode.currentMode.equals("Violet")) {
                animeGirlStr = "girl4";
            } else if (ClickGUI.girlmode.currentMode.equals("Kirshtein")) {
                animeGirlStr = "girl5";
            } else if (ClickGUI.girlmode.currentMode.equals("002")) {
                animeGirlStr = "girl6";
            }
            RenderUtils.drawImage(new ResourceLocation("rich/anime/" + animeGirlStr + ".png"), (float) (sr.getScaledWidth() - 280), (float) (sr.getScaledHeight() - 380 * initAnimation.getOutput()), 280, 380);
        }
        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (!Mouse.isButtonDown(0)) continue;
            imageButton.onClick(mouseX, mouseY);
        }
        //mc.neverlose900_18.drawStringWithShadow("This is free - " + "BETA", 2, sr.getScaledHeight() - 10, new Color(255, 255, 255).getRGB());
        //mc.neverlose900_18.drawStringWithShadow("UID " + "Free", sr.getScaledWidth() - mc.neverlose900_18.getStringWidth("UID " + "Free") - 4, sr.getScaledHeight() - 9, new Color(255, 255, 255).getRGB());
        search.drawTextBox();
        if (search.getText().isEmpty() && !search.isFocused())
            //mc.neverlose900_18.drawStringWithShadow("����� �������...", (sr.getScaledWidth() / 2.0F + 362.0F), 86, -1);


        for (Panel panel : components) {
            panel.drawComponent(sr, mouseX, (int) (mouseY));
            for (ImageButton imageButton : this.imageButtons) {
                imageButton.draw(mouseX, mouseY, Color.WHITE);
                if (!Mouse.isButtonDown(0)) continue;
                imageButton.onClick(mouseX, mouseY);
            }
        }
        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (!Mouse.isButtonDown(0)) continue;
            imageButton.onClick(mouseX, mouseY);
        }
        updateMouseWheel();
        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    public void updateMouseWheel() {
        int scrollWheel = Mouse.getDWheel();
        for (Component panel : components) {
            if (scrollWheel > 0) {
                panel.setY(panel.getY() + 15);
            }
            if (scrollWheel < 0) {
                panel.setY(panel.getY() - 15);
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.selectedPanel.onKeyPress(keyCode);
        if (!escapeKeyInUse)
            super.keyTyped(typedChar, keyCode);
        search.textboxKeyTyped(typedChar, keyCode);
        if ((typedChar == '\t' || typedChar == '\r') && search.isFocused())
            search.setFocused(!search.isFocused());
        escapeKeyInUse = false;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        search.setFocused(false);
        search.setText("");
        search.mouseClicked(mouseX, mouseY, mouseButton);
        for (Component component : components) {
            int x = component.getX();
            int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                ExpandableComponent expandableComponent = (ExpandableComponent) component;
                if (expandableComponent.isExpanded())
                    cHeight = expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
                selectedPanel = component;
                component.onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        selectedPanel.onMouseRelease(state);
    }

    @Override
    public void onGuiClosed() {
        this.screenHelper = new ScreenHelper(0, 0);
        mc.entityRenderer.theShaderGroup = null;
        super.onGuiClosed();
    }
}
