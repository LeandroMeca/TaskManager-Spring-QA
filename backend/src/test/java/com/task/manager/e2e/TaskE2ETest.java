package com.task.manager.e2e;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp(){
        ChromeOptions options = new ChromeOptions();

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // Cria um "esperador" de até 5 segundos para lidar com o tempo de resposta da API
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @Test
    public void acessFrontendSucess(){
        driver.get("http://localhost:4200");

        // 2. O robô encontra os campos, limpa (por garantia) e digita os textos
        WebElement inputTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title")));
        inputTitle.clear(); // Limpa o campo antes de digitar
        inputTitle.sendKeys("Tarefa E2E Automática");
        inputTitle.sendKeys(Keys.TAB); // MÁGICA: Aperta TAB para avisar o Angular que terminou de digitar!

        WebElement inputDesc = driver.findElement(By.name("description"));
        inputDesc.clear();
        inputDesc.sendKeys("Esta tarefa foi digitada sozinha pelo robô do Selenium!");
        inputDesc.sendKeys(Keys.TAB); // Aperta TAB novamente

        // 3. O robô encontra o botão de salvar e clica
        WebElement btnSalvar = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        btnSalvar.click();

        // 4. O robô espera até que o texto da nova tarefa apareça na página inteira
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Tarefa E2E Automática"));

        // 5. Verificação de Sucesso do QA
        assertTrue(driver.getPageSource().contains("Tarefa E2E Automática"), "A tarefa não foi salva na lista!");

    }

    @AfterEach
    public void tearDown(){
        if(driver != null){
            driver.quit();
        }
    }



}
