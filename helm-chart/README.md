### Helm Documentation

### Tiller Permission Issue

```bash
kubectl --namespace kube-system create serviceaccount tiller

kubectl create clusterrolebinding tiller-cluster-rule \
 --clusterrole=cluster-admin --serviceaccount=kube-system:tiller

kubectl --namespace kube-system patch deploy tiller-deploy \
 -p '{"spec":{"template":{"spec":{"serviceAccount":"tiller"}}}}'
 
 ``` 
 
### Creating Internal Helm Charts
1. Install helm to your local
2. Set permissions
    ```bash
    kubectl --namespace kube-system create serviceaccount tiller
    
    kubectl create clusterrolebinding tiller-cluster-rule \
     --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
    
    kubectl --namespace kube-system patch deploy tiller-deploy \
     -p '{"spec":{"template":{"spec":{"serviceAccount":"tiller"}}}}'
     
     ```
3. Install helm push plugin
    ```bash
    helm plugin install https://github.com/chartmuseum/helm-push
    ```
4. Add the private repository to your helm repo list
    ```bash
    helm repo add mychartmuseum http://chartmuseum.example.com
    ```
5. Create a helm chart
    ```bash
    helm create mychart
    ```
    mychart
    ├── Chart.yaml
    ├── charts
    ├── templates
    │   ├── NOTES.txt
    │   ├── _helpers.tpl
    │   ├── deployment.yaml
    │   ├── ingress.yaml
    │   └── service.yaml
    └── values.yaml

6. To set version of the chart, change the "version" key in "Chart.yml" file
7. After you have done with your chart, package your files
    ```bash
    helm package ./mychart
    ```
8. Push the chart to the private repository
    ```bash
    helm push mychart mychartmuseum
    ```        

