package serviceStart;

import java.util.ArrayList;

/**
 * 
 * @author Sajin
 *
 */

public class ServiceVariables {

	private String processType;
	private String readFile;
	private String storeFile;
	private String nerClassifier;
	private String fileType;
	private String performInterlinking;
	private ArrayList<String> silklslFiles;
	private ArrayList<String> silkGeneratedFiles;
	private String storeFirstLinkFile;
	private String performRedirect;
	private String storeRedirectFile;
	private String performSecondInterlinking;
	private String storeSecondLinkFile;
	private String domainEntity;

	public ServiceVariables() {}

	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getProcessType() {
		return processType;
	}
	
	public void setReadFile(String readFile) {
		this.readFile = readFile;
	}
	public String getReadFile() {
		return readFile;
	}

	public void setStoreFile(String storeFile) {
		this.storeFile = storeFile;
	}
	public String getStoreFile() {
		return storeFile;
	}

	public void setNerClassifier(String nerClassifier) {
		this.nerClassifier = nerClassifier;
	}
	public String getNerClassifier() {
		return nerClassifier;
	}
	
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileType() {
		return fileType;
	}
	
	public void setPerformInterlinking(String performInterlinking) {
		this.performInterlinking = performInterlinking;
	}
	public String getPerformInterlinking() {
		return performInterlinking;
	}

	public void setSilkLslFiles(ArrayList<String> silklslFiles) {
		this.silklslFiles = silklslFiles;
	}
	public ArrayList<String> getSilkLslFiles() {
		return silklslFiles;
	}
	
	public void setSilkGeneratedFiles(ArrayList<String> silkGeneratedFiles) {
		this.silkGeneratedFiles = silkGeneratedFiles;
	}
	public ArrayList<String> getSilkGeneratedFiles() {
		return silkGeneratedFiles;
	}
	
	public void setStoreFirstLinkFile(String storeFirstLinkFile) {
		this.storeFirstLinkFile = storeFirstLinkFile;
	}
	public String getStoreFirstLinkFile() {
		return storeFirstLinkFile;
	}
	
	public void setPerformRedirect(String performRedirect) {
		this.performRedirect = performRedirect;
	}
	public String getPerformRedirect() {
		return performRedirect;
	}
	
	public void setStoreRedirectFile(String storeRedirectFile) {
		this.storeRedirectFile = storeRedirectFile;
	}
	public String getStoreRedirectFile() {
		return storeRedirectFile;
	}
	
	public void setPerformSecondInterlinking(String performSecondInterlinking) {
		this.performSecondInterlinking = performSecondInterlinking;
	}
	public String getPerformSecondInterlinking() {
		return performSecondInterlinking;
	}
	
	public void setStoreSecondLinkFile(String storeSecondLinkFile) {
		this.storeSecondLinkFile = storeSecondLinkFile;
	}
	public String getStoreSecondLinkFile() {
		return storeSecondLinkFile;
	}

	public void setDomainEntity(String domainEntity) {
		this.domainEntity = domainEntity;
	}
	public String getDomainEntity() {
		return domainEntity;
	}

}
