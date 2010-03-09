package app.web.panel;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.data.Address;
import app.data.Country;

import com.avaje.ebean.Ebean;

/**
 * Homepage
 */
public class AddressPanel extends Panel {
	private static Logger log = LoggerFactory.getLogger(AddressPanel.class);
	private static final long serialVersionUID = 1L;

	// TODO Add any page properties or variables here

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public AddressPanel(String id) {
		this(id, new Model<Address>(new Address()));
	}

	public AddressPanel(String id, IModel<Address> model) {
		super(id, (model instanceof CompoundPropertyModel ? model : new CompoundPropertyModel(model
				.getObject())));
		add(new DropDownChoice("country", //
				new PropertyModel(model.getObject(), "country"), //
				(List) Ebean.find(Country.class).findList(), //
				new ChoiceRenderer("name")));
		add(new TextField("line1"));
		add(new TextField("line2"));
		add(new TextField("city"));
	}
}
