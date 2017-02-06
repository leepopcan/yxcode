package protoarchi.mvc;

/**
 * Base for all controllers.
 * This interface is to make a standard for all controllers. Each controller should be bind with
 * BaseView and BaseModel.
 */
public abstract class IController<V extends IView, M extends IModel> {

  /**
   * before data coming, bind some constants to view
   * 
   * @param view
   */
  public void preBind(V view) {}

  /**
   * Bind the view and model.
   *
   * @param view view
   * @param model model
   */
  public abstract void bind(V view, M model);

  /**
   * Initialize/reset the view and model for reuse, called before bind in ListView , called when
   * view recycle in RecyclerView.
   * 
   * Your need override this method if something is running when view is recycle.
   */
  public void unbind() {}

}
