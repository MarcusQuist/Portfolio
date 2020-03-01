import React from 'react';
import styles from './PopupMenu.module.css';

class PopupMenu extends React.Component {
    constructor(props){
        super(props);
        this.longTerm = this.longTerm.bind(this);
        this.mediumTerm = this.mediumTerm.bind(this);
        this.shortTerm = this.shortTerm.bind(this);
    }

    search(){
        this.props.onSearch(this.state.term);
    }

    longTerm(){
        this.props.generatePlaylist('long_term');
        this.props.popupMenu();
    }

    mediumTerm(){
        this.props.generatePlaylist('medium_term');
        this.props.popupMenu();
    }

    shortTerm(){
        this.props.generatePlaylist('short_term');
        this.props.popupMenu();
    }


    render() {
      return (
        <div className={styles.container}>
            <div className={styles.box}> 
                <button className={styles.closeButton}
                        onClick={this.props.popupMenu}>X</button>
                <h2 className={styles.centerText + ' ' + styles.marginBottom}>Generate a personalized playlist of your top songs according to your recent behaviour!</h2>
                <div className={styles.wrapperContainer}>
                    <div className={styles.subContainer}>
                        <h3 className={styles.centerText}>Last couple of years</h3>
                        <button className={styles.searchButton}
                                onClick={this.longTerm}>Generate</button>
                    </div>
                    <div className={styles.subContainer}>
                        <h3 className={styles.centerText}>Last 6 months</h3>
                        <button className={styles.searchButton}
                                onClick={this.mediumTerm}>Generate</button>
                    </div>
                    <div className={styles.subContainer}>
                        <h3 className={styles.centerText}>Last 4 weeks</h3>
                        <button className={styles.searchButton}
                                onClick={this.shortTerm}>Generate</button>
                    </div>
                </div>
            </div>
        </div>
        );
    }
  }
export default PopupMenu;