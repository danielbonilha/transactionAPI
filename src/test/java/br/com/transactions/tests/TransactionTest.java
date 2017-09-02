package br.com.transactions.tests;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionTest {

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
	
	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation))
				.build();
	}
	
	
	@Test
	public void t01_postOneTransactionSucess() throws Exception {
		String jsonRequest = getResourceAsString("json/test01/request.json");
		String jsonResponse = getResourceAsString("json/test01/response.json");
		this.mockMvc.perform(
			post("/v1/transactions")
				.content(jsonRequest)
				.contentType(MediaTypes.HAL_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().json(jsonResponse))
				.andDo(document("transactions/post/sucess", 
						preprocessRequest(prettyPrint()), 
						preprocessResponse(prettyPrint()),
                        requestFields(
                        		attributes(key("title").value("Campos para criação do objeto Transactions")),
                        		fieldWithPath("accountId").description("Id da Account")
                        			.attributes(key("constraints").value("Campo obrigatório.")),
                        		fieldWithPath("amount").description("Valor da transação")
                        			.attributes(key("constraints").value("Campo obrigatório.")),
                        		fieldWithPath("operationType").description("Tipo de Operação")
                        			.attributes(key("constraints").value("Campo opcional."))
                        )));
	}
	
	@Test
	public void t02_postOneTransactionFail() throws Exception {
		String jsonRequest = getResourceAsString("json/test02/request.json");
		String jsonResponse = getResourceAsString("json/test02/response.json");
		this.mockMvc.perform(
				post("/v1/transactions")
					.content(jsonRequest)
					.contentType(MediaTypes.HAL_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(content().json(jsonResponse))
					.andDo(document("transactions/post/fail", 
							preprocessRequest(prettyPrint()), 
							preprocessResponse(prettyPrint()),
	                        responseFields(
	                                fieldWithPath("id").description("Id do objeto em questão (se houver)"),
	                                fieldWithPath("errorMessage").description("Descrição dos erros, separados por vírgula")
	                        )));
	}
	
	
	@Test
	public void t03_getAllTransactions() throws Exception {
		String jsonResponse = getResourceAsString("json/test03/response.json");
		this.mockMvc.perform(
			get("/v1/transactions")
				.accept(MediaTypes.HAL_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonResponse))
				.andDo(document("transactions/get/sucess", 
						preprocessRequest(prettyPrint()), 
						preprocessResponse(prettyPrint())));
	}
	
	
	@Test
	public void t04_postOnePaymentSucess() throws Exception {
		String jsonRequest = getResourceAsString("json/test04/request.json");
		this.mockMvc.perform(
			post("/v1/payments")
				.content(jsonRequest)
				.contentType(MediaTypes.HAL_JSON))
				.andExpect(status().isNoContent())
				.andDo(document("payments/post/sucess", 
						preprocessRequest(prettyPrint()), 
						preprocessResponse(prettyPrint())));
	}

	
	private String getResourceAsString(String path) throws IOException {
		StringBuilder result = new StringBuilder();
		try (Scanner scanner = new Scanner(new ClassPathResource(path).getFile())) {
			while (scanner.hasNextLine()) {
				result.append(scanner.nextLine()).append("\n");
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
}
