package view;

/**
 * Interface representing operations supported by an event-emitting view (i.e. one used as an {@link
 * java.awt.event.ActionListener}). Action-listening views should support registering any number of
 * event listeners and emitting events to all of them. Examples of event listeners are controllers
 * that transmit GUI events to the model (i.e. {@link controller.FrameController}.
 */
public interface IImageEventView extends IImageView {

  /**
   * Register a new event listener that is triggered every time an event is emitted. This listener
   * must contain methods supporting all possible events, defined in the {@link IViewListener}
   * interface.
   *
   * <p>Any number of event listeners can be registered to a view and all will receive emitted
   * events.
   *
   * @param listener the event listener to register with this view with methods for handling all
   *                 emitted events.
   */
  void registerViewEventListener(IViewListener listener);
}
