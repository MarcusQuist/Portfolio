import React from 'react';
import styles from './Notify.module.css';

class Notify extends React.Component {
    constructor(props){
        super(props);

    }

    render() {
      return (
        <div className={styles.container}>
            <div className={styles.box}> 
                <button className={styles.closeButton}
                        onClick={this.props.close}>X</button>
                <h2 className={styles.centerText}>{this.props.title}</h2>
                <h3 className={styles.centerText}>{this.props.message}</h3>
            </div>
        </div>
        );
    }
  }
export default Notify;