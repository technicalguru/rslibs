package rs.restclient.springboot;

import java.io.IOException;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import rs.baselib.util.CommonUtils;
import rs.baselib.util.UriUtils;
import rs.restclient.core.util.Form;

/**
 * Helper class and template for Spring Boot message writers to convert {@link Form}s.
 * @author ralph
 *
 */
public class FormMessageConverter implements HttpMessageConverter<Form> {

	/**
	 * Default constructor.
	 */
	public FormMessageConverter() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
		if (!clazz.isAssignableFrom(Form.class)) return false;
		return MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return CommonUtils.newList(MediaType.APPLICATION_FORM_URLENCODED);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Form read(Class<? extends Form> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Form t, @Nullable MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		if (t != null) {
			outputMessage.getBody().write(UriUtils.encodeQuery(t.asMap()).getBytes());
		}
	}

	
}
