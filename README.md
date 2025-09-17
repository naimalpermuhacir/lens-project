# OpenLens Android

OpenLens'in Android versiyonu - Kubernetes cluster'larınızı mobil cihazınızdan yönetin.

## 🚀 Özellikler

### Cluster Yönetimi
- Multiple Kubernetes cluster desteği
- Kubeconfig import/export
- Cluster bağlantı durumu takibi
- Real-time cluster health monitoring

### Workload Yönetimi
- Pod görüntüleme ve yönetimi
- Deployment, StatefulSet, DaemonSet yönetimi
- ReplicaSet ve Job yönetimi
- Real-time pod durumu takibi

### Service Yönetimi
- Service ve Ingress yönetimi
- LoadBalancer ve NodePort servisleri
- DNS ve port yapılandırmaları

### Config Yönetimi
- ConfigMap yönetimi
- Secret yönetimi
- Environment variable yönetimi

### Monitoring & Logs
- Real-time pod log görüntüleme
- Resource metrics
- Event monitoring
- Terminal erişimi

### UI/UX
- Modern Material Design 3 arayüz
- Dark/Light theme desteği
- Responsive tasarım
- Intuitive navigation

## 🛠️ Teknoloji Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Repository Pattern
- **Dependency Injection**: Hilt
- **Database**: Room
- **Networking**: Retrofit + OkHttp
- **Async**: Coroutines + Flow
- **Navigation**: Navigation Compose

## 📱 Minimum Gereksinimler

- Android 7.0 (API 24) ve üzeri
- 2GB RAM
- 100MB boş alan

## 🚀 Kurulum

1. Projeyi klonlayın:
```bash
git clone https://github.com/yourusername/lens-project.git
```

2. Android Studio'da projeyi açın

3. Gradle sync yapın

4. Uygulamayı çalıştırın

## 📖 Kullanım

### Cluster Ekleme
1. Ana ekranda "+" butonuna tıklayın
2. Cluster adını girin
3. Kubeconfig dosyasını yapıştırın
4. "Add" butonuna tıklayın

### Pod Yönetimi
1. "Workloads" sekmesine gidin
2. Pod listesini görüntüleyin
3. Pod'a tıklayarak detayları görün
4. Logs ve terminal erişimi için menüyü kullanın

### Service Yönetimi
1. "Services" sekmesine gidin
2. Service listesini görüntüleyin
3. Service detaylarını inceleyin
4. Port ve endpoint bilgilerini kontrol edin

## 🔧 Geliştirme

### Proje Yapısı
```
app/
├── src/main/java/com/openlens/android/
│   ├── data/           # Data layer
│   ├── di/             # Dependency injection
│   ├── ui/             # UI components
│   └── utils/          # Utility classes
```

### Build
```bash
./gradlew build
```

### Test
```bash
./gradlew test
```

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit yapın (`git commit -m 'Add amazing feature'`)
4. Push yapın (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.

## 🙏 Teşekkürler

- [OpenLens](https://github.com/MuhammedKalkan/OpenLens) - Orijinal OpenLens projesi
- [Kubernetes](https://kubernetes.io/) - Container orchestration platform
- [Android Jetpack](https://developer.android.com/jetpack) - Android development libraries

## 📞 İletişim

- GitHub: [@yourusername](https://github.com/yourusername)
- Email: your.email@example.com

## 🐛 Bug Reports

Bug raporları için [Issues](https://github.com/yourusername/lens-project/issues) sayfasını kullanın.

## 📈 Roadmap

- [ ] Real-time metrics dashboard
- [ ] Advanced terminal features
- [ ] Custom resource definitions (CRD) support
- [ ] Multi-cluster monitoring
- [ ] Offline mode improvements
- [ ] Widget support
- [ ] Wear OS companion app

---

**Not**: Bu proje OpenLens'in Android versiyonudur ve Kubernetes cluster yönetimi için tasarlanmıştır.