/**
 */
package org.eclipse.emf.cdo.releng.setup;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mylyn Query Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.cdo.releng.setup.MylynQueryTask#getConnectorKind <em>Connector Kind</em>}</li>
 *   <li>{@link org.eclipse.emf.cdo.releng.setup.MylynQueryTask#getSummary <em>Summary</em>}</li>
 *   <li>{@link org.eclipse.emf.cdo.releng.setup.MylynQueryTask#getURL <em>URL</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.cdo.releng.setup.SetupPackage#getMylynQueryTask()
 * @model
 * @generated
 */
public interface MylynQueryTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Connector Kind</b></em>' attribute.
   * The default value is <code>"bugzilla"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Connector Kind</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Connector Kind</em>' attribute.
   * @see #setConnectorKind(String)
   * @see org.eclipse.emf.cdo.releng.setup.SetupPackage#getMylynQueryTask_ConnectorKind()
   * @model default="bugzilla" required="true"
   * @generated
   */
  String getConnectorKind();

  /**
   * Sets the value of the '{@link org.eclipse.emf.cdo.releng.setup.MylynQueryTask#getConnectorKind <em>Connector Kind</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Connector Kind</em>' attribute.
   * @see #getConnectorKind()
   * @generated
   */
  void setConnectorKind(String value);

  /**
   * Returns the value of the '<em><b>Summary</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Summary</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Summary</em>' attribute.
   * @see #setSummary(String)
   * @see org.eclipse.emf.cdo.releng.setup.SetupPackage#getMylynQueryTask_Summary()
   * @model required="true"
   * @generated
   */
  String getSummary();

  /**
   * Sets the value of the '{@link org.eclipse.emf.cdo.releng.setup.MylynQueryTask#getSummary <em>Summary</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Summary</em>' attribute.
   * @see #getSummary()
   * @generated
   */
  void setSummary(String value);

  /**
   * Returns the value of the '<em><b>URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>URL</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>URL</em>' attribute.
   * @see #setURL(String)
   * @see org.eclipse.emf.cdo.releng.setup.SetupPackage#getMylynQueryTask_URL()
   * @model required="true"
   *        extendedMetaData="kind='attribute' name='url'"
   * @generated
   */
  String getURL();

  /**
   * Sets the value of the '{@link org.eclipse.emf.cdo.releng.setup.MylynQueryTask#getURL <em>URL</em>}' attribute.
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>URL</em>' attribute.
   * @see #getURL()
   * @generated
   */
  void setURL(String value);

} // MylynQueryTask