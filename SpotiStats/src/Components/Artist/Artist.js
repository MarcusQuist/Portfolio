import React from 'react';
import styles from './Artist.module.css';

class Artist extends React.Component{
    constructor(props){
        super(props);
        this.addArtist = this.addArtist.bind(this);
        this.removeArtist = this.removeArtist.bind(this);
    }

    renderAction(){
        if(this.props.isRemoval){
            return <button className={styles.ArtistAction}
                            onClick={this.removeArtist}>-</button>;
        }
        else{
            return <button className={styles.ArtistAction}
                           onClick={this.addArtist}>+</button>;
        }
    }

    addArtist(){
        this.props.onAdd(this.props.artist);
    }

    removeArtist(){
        this.props.onRemove(this.props.artist);
    }


    render(){
        return(
            <div className={styles.Artist}>
                <div className={styles.ArtistInformation}>
                    <img src={this.props.url}
                          className={styles.ArtistImg}  
                            ></img>
                    <h3>{this.props.name}</h3>
                </div>
                {this.renderAction()}
            </div>
        )
    }
}

export default Artist;

